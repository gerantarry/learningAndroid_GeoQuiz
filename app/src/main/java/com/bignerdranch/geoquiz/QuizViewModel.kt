package com.bignerdranch.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel (){

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_asia,true),
        Question(R.string.question_americas,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_oceans,true),
        Question(R.string.question_africa,false)
    )

    var currentIndex = 0

    var currentCheaterStatus: Boolean
    get() = questionBank[currentIndex].isCheater
    set(isCheater:Boolean) { questionBank[currentIndex].isCheater = isCheater }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }


}