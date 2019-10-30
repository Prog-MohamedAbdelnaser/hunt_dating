package com.recep.hunt.model.UserProfile

import com.recep.hunt.model.RegistrationModule.User

data class Data(
val user_info: UserInfoModel,
val social_info: Any,
val user_profile_image:ArrayList<String>
)