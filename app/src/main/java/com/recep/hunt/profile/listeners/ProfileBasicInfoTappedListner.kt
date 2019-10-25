package com.recep.hunt.profile.listeners

import com.recep.hunt.profile.model.UserBasicInfoQuestionModel


/**
 * Created by Rishabh Shukla
 * on 2019-09-04
 * Email : rishabh1450@gmail.com
 */

interface ProfileBasicInfoTappedListner {
    fun onItemClicked(position:Int, questionModel: UserBasicInfoQuestionModel, icon:Int)
}