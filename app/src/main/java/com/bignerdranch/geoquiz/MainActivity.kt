package com.bignerdranch.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
    private lateinit var resetButton:Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView:TextView

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_asia,true),
        Question(R.string.question_americas,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_oceans,true),
        Question(R.string.question_africa,false)
    )

    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        resetButton = findViewById(R.id.reset_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            getUserScore()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            getUserScore()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener {
            currentIndex = when {
                currentIndex == 0 -> questionBank.lastIndex
                else -> currentIndex - 1
            }
            updateQuestion()
        }

        resetButton.setOnClickListener {
            questionBank.forEach { it.userAnswer = null }
            setButtonState()
            score = 0
        }

        questionTextView.setOnClickListener{
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun setUserAnswer(userAnswer: Boolean){
        if (questionBank[currentIndex].answer == userAnswer)
            score++
        questionBank[currentIndex].userAnswer = userAnswer
        setButtonState()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        setButtonState()
    }

    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        setUserAnswer(userAnswer)
        val messageResId = if (userAnswer == correctAnswer)
            R.string.correct_toast
        else R.string.incorrect_toast

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT)
            .show()
        }

    private fun setButtonState(){
        if (questionBank[currentIndex].userAnswer != null){
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }
        else
        {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun getUserScore(){
        if (questionBank.all{it.userAnswer != null})
                Toast.makeText(
                    this,
                    "Your score: $score/6",
                    Toast.LENGTH_LONG)
                    .show()
    }
}