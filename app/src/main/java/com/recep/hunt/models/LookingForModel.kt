package com.recep.hunt.models

data class LookingForModel(val unSelectedImage:Int,
                           val selectedImage:Int,
                           val label:String,
                           var isSelected:Boolean,
                           var value:String?)