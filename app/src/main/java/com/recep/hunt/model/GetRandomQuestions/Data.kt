package com.recep.hunt.model.GetRandomQuestions

data class Data(
    val question: String,
    val answer: ArrayList<String>,
    val icon : String ,
    val  main_question : String ,
    val type : String
)