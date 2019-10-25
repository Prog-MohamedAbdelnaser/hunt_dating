package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("height")
    val height: Int, // 3024
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String?, // CmRaAAAAf9_xFNiBVLGVCo3WVjpgqXJNPvTeNf01VFwvV1KN9BR55IX_-PpIqOcPJM_rBKT6_xuSuN67Afhh_K2GD6q2fl54WhIkikjeMZsIm2q_Xy5QoCmjmoGLB07nEkcMiP_GEhBtnrIlVgpXPMNR0hCDBPoWGhSppRw5bJ6GMD0KoosdWj6IjYOtEw
    @SerializedName("width")
    val width: Int // 4032
)