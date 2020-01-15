package com.recep.hunt.userDetail.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rishabh Shukla
 * on 2019-09-20
 * Email : rishabh1450@gmail.com
 */

@Parcelize
class TimelineModel(
    var title: String,
    var message: String

) : Parcelable
