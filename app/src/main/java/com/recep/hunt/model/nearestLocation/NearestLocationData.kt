package com.recep.hunt.model.nearestLocation

data class NearestLocationData(
    val name: String,
    val address: String,
    val place_id:String ,
    val lat : Double ,
    val lang : Double,
    val distance : Double ,
    val image : String ,
    val users : Int
)