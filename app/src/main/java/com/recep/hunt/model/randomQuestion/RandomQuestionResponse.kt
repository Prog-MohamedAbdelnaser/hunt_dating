package com.recep.hunt.model.randomQuestion

data class RandomQuestionResponse(

    val data: QuestionData,
    val message: String,
    val status: Int
)