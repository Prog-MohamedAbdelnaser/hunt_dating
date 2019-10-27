package com.recep.hunt.model.nearestLocation

data class Data(
    val address: String,
    val name: String,
    val place_id:String ,
    val lat : Double ,
    val lang : Double,
    val distance : Double ,
    val image : String ,
    val users : Int
)