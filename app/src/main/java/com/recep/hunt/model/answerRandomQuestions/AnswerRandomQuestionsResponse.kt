package com.recep.hunt.model.answerRandomQuestions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerRandomQuestionsResponse(
    val data: Any,
    val message: String,
    val status: Int
)