package com.bignerdranch.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val TAG = "CheatActivity"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val KEY_CHEATER = "isCheater"

class CheatActivity : AppCompatActivity() {
    private lateinit var apiVersionTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView
    private var answerIsTrue = false
    private var isCheater = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        isCheater = savedInstanceState?.getBoolean(KEY_CHEATER,false) ?: false //загружаем из предыдущего состояние статус читера
        setAnswerShownResult(isCheater)

        //достаём значение переданное в MainActivity
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiVersionTextView = findViewById(R.id.api_version_text_view)

        /**
         * определяем текст в TextView
         */
        showAnswerButton.setOnClickListener {
            val answerText = when{
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            isCheater = true
            setAnswerShownResult(isCheater)
        }
        val textApiLevel = getString(R.string.api_level_text, Build.VERSION.SDK_INT)
        apiVersionTextView.text = textApiLevel


    // сохраняем в состоянии статус читера
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putBoolean(KEY_CHEATER, isCheater)
    }

    /**
     * передаём в родительскую активити признак подсмотренного
     * ответа через Intent
     */
    private fun setAnswerShownResult(isCheater:Boolean){
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN, isCheater)
        }
        setResult(Activity.RESULT_OK,data)
    }

    /**
     * инкапсуляция функции создания интента, для передачи данных между активити
     */
    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean):Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
            }
        }
    }
}