package com.recep.hunt.contactUs.model

import android.graphics.Bitmap
import android.net.Uri


/**
 * Created by Rishabh Shukla
 * on 2019-09-09
 * Email : rishabh1450@gmail.com
 */

data class ChatModel(
    val msgData:String="",
    val msgType:Int,
    val bitmapImage: Bitmap? =null,
    val uriGif: Uri? =null,
    val type:String)