package com.bignerdranch.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "CheatActivity"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
const val EXTRA_HINTS_COUNTER = "com.bignerdranch.android.geoquiz.hints_counter"
private const val KEY_CHEATER = "isCheater"
private const val KEY_HINTS_COUNTER = "hintsCounter"

class CheatActivity : AppCompatActivity() {
    private lateinit var apiVersionTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView
    private lateinit var hintsCounterTextView:TextView
    private var answerIsTrue = false
    private var isCheater = false
    private var hintsCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        isCheater = savedInstanceState?.getBoolean(KEY_CHEATER, false) ?: false //загружаем из предыдущего состояние статус читера
        //достаём значение переданное в MainActivity
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        //hintsCounter = intent.getIntExtra(EXTRA_HINTS_COUNTER, 0)
        hintsCounter = savedInstanceState?.getInt(KEY_HINTS_COUNTER, 0) ?: intent.getIntExtra(EXTRA_HINTS_COUNTER, 0) //загружаем кол-во подсказок доступное пользователю
        setAnswerShownResult(isCheater)
        //инициализация элементов отображения
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiVersionTextView = findViewById(R.id.api_version_text_view)
        hintsCounterTextView = findViewById(R.id.hints_counter_text_view)
        checkHintsCount()
        /**
         * определяем текст в answerTextView
         */
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            isCheater = true
            hintsCounter--
            setAnswerShownResult(isCheater)
            checkHintsCount()
        }

        val textApiLevel = getString(R.string.api_level_text, Build.VERSION.SDK_INT)
        apiVersionTextView.text = textApiLevel
        setHintCounterText()
    }

    // сохраняем в состоянии статус читера
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putBoolean(KEY_CHEATER, isCheater)
        savedInstanceState.putInt(KEY_HINTS_COUNTER, hintsCounter)
    }

    /**
     * передаём в родительскую активити признак подсмотренного
     * ответа через Intent
     */
    private fun setAnswerShownResult(isCheater: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isCheater)
            putExtra(EXTRA_HINTS_COUNTER, hintsCounter)
            Log.d(TAG,"setAnswerShownResult hintsCounter value = $hintsCounter")
        }
        setResult(Activity.RESULT_OK, data)
    }
    //проверить кол-во подсказок, заблокировать кнопку
    private fun checkHintsCount(){
        setHintCounterText()
        if (hintsCounter<=0) showAnswerButton.isEnabled = false
    }
    //обновить текст в hintsCounterTextView
    private fun setHintCounterText(){
        val textHintsCounter = getString(R.string.hints_left_text, hintsCounter)
        hintsCounterTextView.text = textHintsCounter
    }

    /**
     * инкапсуляция функции создания интента, для передачи данных между активити
     */
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, hintsCounter:Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_HINTS_COUNTER, hintsCounter)
            }
        }
    }
}
