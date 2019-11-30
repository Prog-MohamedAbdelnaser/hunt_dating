package com.recep.hunt.model.nearestLocation

data class NearestLocationResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<NearestLocationData>
)