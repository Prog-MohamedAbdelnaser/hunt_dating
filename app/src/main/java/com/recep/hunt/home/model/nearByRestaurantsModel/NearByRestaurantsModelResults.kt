package com.recep.hunt.home.model.nearByRestaurantsModel


import com.google.gson.annotations.SerializedName

data class NearByRestaurantsModelResults(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("icon")
    val icon: String, // https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png
    @SerializedName("id")
    val id: String, // c54313c27efc4117124edcc3a6b1daafb2d6033e
    @SerializedName("name")
    val name: String, // The Squire's Landing
    @SerializedName("opening_hours")
    val openingHours: OpeningHours,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("place_id")
    val placeId: String, // ChIJDcRr3A6vEmsRN3dKYsaQawI
    @SerializedName("plus_code")
    val plusCode: PlusCode,
    @SerializedName("price_level")
    val priceLevel: Int, // 2
    @SerializedName("rating")
    val rating: Double, // 4.2
    @SerializedName("reference")
    val reference: String, // ChIJDcRr3A6vEmsRN3dKYsaQawI
    @SerializedName("scope")
    val scope: String, // GOOGLE
    @SerializedName("types")
    val types: List<String>,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int, // 654
    @SerializedName("vicinity")
    val vicinity: String // Northern end of the Overseas Passenger Terminal, Circular Quay W, The Rocks
)