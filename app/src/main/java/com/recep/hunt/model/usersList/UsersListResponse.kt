package com.recep.hunt.model.usersList

data class UsersListResponse (
    val data: ArrayList<Data>,
    val message : String,
    val status : Int
)