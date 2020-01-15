package com.recep.hunt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerRandomQuestions(
    @Expose
    @SerializedName("question")
    val question: String,

    @Expose
    @SerializedName("answer")
    val answer: String
)