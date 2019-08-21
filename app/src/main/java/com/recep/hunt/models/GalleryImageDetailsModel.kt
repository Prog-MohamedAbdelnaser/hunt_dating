package com.recep.hunt.models

import android.net.Uri


//TODO Dummy Model --- please fix when picture taken from device
data class GalleryImageDetailsModel(val title:String,
                                    val uri: String,
                                    var count:Int
)