package com.recep.hunt.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rishabh Shukla
 * on 2019-09-03
 * Email : rishabh1450@gmail.com
 */

@Parcelize
data class UserBasicInfoQuestionModel(val question:Int,
                                      val isListTypeQuestion:Boolean,
                                      val options:ArrayList<Int>?,
                                      val optionPlaceholder:Int?,
                                      val selectedValue:Int?):Parcelable