package com.recep.hunt.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.recep.hunt.R
import com.recep.hunt.model.usersList.BasicInfo
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.utilis.SharedPrefrenceManager


/**
 * Created by Rishabh Shukla
 * on 2019-09-16
 * Email : rishabh1450@gmail.com
 */

class BasicInfoViewModel(private val app: Application) {
    companion object {
        fun getInstace(app: Application): BasicInfoViewModel = BasicInfoViewModel(app)
    }

    fun getData(): ArrayList<UserBasicInfoModel> {
        val basicModel = ArrayList<UserBasicInfoModel>()
        val questionModel = getBasicInfoQuestions()
        if (basicModel.size == 0) {
            basicModel.add(
                UserBasicInfoModel(
                    "Relationship status",
                    SharedPrefrenceManager.getRelationShipStatus(app.applicationContext),
                    R.drawable.relationship_icon,
                    questionModel[0]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Height",
                    SharedPrefrenceManager.getUserHeight(app.applicationContext),
                    R.drawable.height_icon,
                    questionModel[1]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Gym",
                    SharedPrefrenceManager.getUserGym(app.applicationContext),
                    R.drawable.gym_icon,
                    questionModel[2]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Education level",
                    SharedPrefrenceManager.getUserEducationLevel(app.applicationContext),
                    R.drawable.education_icon,
                    questionModel[3]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Drink",
                    SharedPrefrenceManager.getUserDrink(app.applicationContext),
                    R.drawable.drink_icon,
                    questionModel[4]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Smoke",
                    SharedPrefrenceManager.getSomke(app.applicationContext),
                    R.drawable.smoke_icon,
                    questionModel[5]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Pets",
                    SharedPrefrenceManager.getPets(app.applicationContext),
                    R.drawable.pets_icon,
                    questionModel[6]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Looking for",
                    SharedPrefrenceManager.getLookingFor(app.applicationContext),
                    R.drawable.looking_for_icon,
                    questionModel[7]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Kids",
                    SharedPrefrenceManager.getKids(app.applicationContext),
                    R.drawable.kids_icon,
                    questionModel[8]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Zodiac",
                    SharedPrefrenceManager.getZodiac(app.applicationContext),
                    R.drawable.zodiac_icon,
                    questionModel[9]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Religion",
                    SharedPrefrenceManager.getReligion(app.applicationContext),
                    R.drawable.religion_icon,
                    questionModel[10]
                )
            )
        }
        return basicModel
    }

    fun getData(basicInfo: BasicInfo?): ArrayList<UserBasicInfoModel> {
        val basicModel = ArrayList<UserBasicInfoModel>()
        val questionModel = getBasicInfoQuestions(basicInfo)
        if (basicModel.size == 0) {
            basicModel.add(
                UserBasicInfoModel(
                    "Relationship status",
                    basicInfo?.about,
                    R.drawable.relationship_icon,
                    questionModel[0]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Height",
                    basicInfo?.height,
                    R.drawable.height_icon,
                    questionModel[1]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Gym",
                    basicInfo?.gym,
                    R.drawable.gym_icon,
                    questionModel[2]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Education level",
                    basicInfo?.education_level,
                    R.drawable.education_icon,
                    questionModel[3]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Drink",
                    basicInfo?.drink,
                    R.drawable.drink_icon,
                    questionModel[4]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Smoke",
                    basicInfo?.smoke,
                    R.drawable.smoke_icon,
                    questionModel[5]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Pets",
                    basicInfo?.pets,
                    R.drawable.pets_icon,
                    questionModel[6]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Looking for",
                    "",
                    R.drawable.looking_for_icon,
                    questionModel[7]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Kids",
                    basicInfo?.kids,
                    R.drawable.kids_icon,
                    questionModel[8]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Zodiac",
                    basicInfo?.zodiac,
                    R.drawable.zodiac_icon,
                    questionModel[9]
                )
            )
            basicModel.add(
                UserBasicInfoModel(
                    "Religion",
                    basicInfo?.religion,
                    R.drawable.religion_icon,
                    questionModel[10]
                )
            )
        }
        return basicModel
    }

    fun getBasicInfoQuestions( basicInfo: BasicInfo?): ArrayList<UserBasicInfoQuestionModel> {
        val questionModel = ArrayList<UserBasicInfoQuestionModel>()
        if (questionModel.size == 0) {
            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.relation_question,
                    true,
                    arrayListOf(
                        R.string.relation_option_1,
                        R.string.relation_option_2,
                        R.string.relation_option_3,
                        R.string.relation_option_4,
                        R.string.relation_option_5
                    ),
                    null,
                   "Single"
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.height_question,
                    false,
                    null,
                    R.string.height_placeholder,
                   basicInfo?.height
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.gym_question,
                    true,
                    arrayListOf(
                        R.string.gym_option_1,
                        R.string.gym_option_2,
                        R.string.gym_option_3
                    ),
                    null,
                   basicInfo?.gym
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.edutcation_question,
                    true,
                    arrayListOf(
                        R.string.education_option_1,
                        R.string.education_option_2,
                        R.string.education_option_3,
                        R.string.education_option_4,
                        R.string.education_option_5,
                        R.string.education_option_6
                    ),
                    null,
                   basicInfo?.education_level
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.drink_question,
                    true,
                    arrayListOf(
                        R.string.drink_option_1,
                        R.string.drink_option_2,
                        R.string.drink_option_3
                    ),
                    null,
                    basicInfo?.drink
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.smoke_question,
                    true,
                    arrayListOf(
                        R.string.smoke_option_1,
                        R.string.smoke_option_2,
                        R.string.smoke_option_3
                    ),
                    null,
                   basicInfo?.smoke
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.pets_question,
                    true,
                    arrayListOf(
                        R.string.pet_option_1,
                        R.string.pet_option_2,
                        R.string.pet_option_3,
                        R.string.pet_option_4,
                        R.string.pet_option_5,
                        R.string.pet_option_6
                    ),
                    null,
                   basicInfo?.pets
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.looking_for_question,
                    true,
                    arrayListOf(
                        R.string.looking_for_option_1,
                        R.string.looking_for_option_2,
                        R.string.looking_for_option_3,
                        R.string.looking_for_option_4
                    ),
                    null,
                    basicInfo?.zodiac
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.kids_question,
                    true,
                    arrayListOf(
                        R.string.kids_option_1,
                        R.string.kids_option_2,
                        R.string.kids_option_3,
                        R.string.kids_option_4
                    ),
                    null,
                    basicInfo?.kids
                )
            )




            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.zodiac_question,
                    true,
                    arrayListOf(
                        R.string.zodiac_option_1,
                        R.string.zodiac_option_2,
                        R.string.zodiac_option_3,
                        R.string.zodiac_option_4,
                        R.string.zodiac_option_5,
                        R.string.zodiac_option_6,
                        R.string.zodiac_option_7,
                        R.string.zodiac_option_8,
                        R.string.zodiac_option_9,
                        R.string.zodiac_option_10,
                        R.string.zodiac_option_11,
                        R.string.zodiac_option_12
                    ),
                    null,
                    basicInfo?.zodiac
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.religion_question,
                    true,
                    arrayListOf(
                        R.string.religin_option_1,
                        R.string.religin_option_2,
                        R.string.religin_option_3,
                        R.string.religin_option_4,
                        R.string.religin_option_5,
                        R.string.religin_option_6
                    ),
                    null,
                    basicInfo?.religion
                )
            )

        }

        return questionModel
    }

    fun getBasicInfoQuestions(): ArrayList<UserBasicInfoQuestionModel> {
        val questionModel = ArrayList<UserBasicInfoQuestionModel>()
        if (questionModel.size == 0) {
            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.relation_question,
                    true,
                    arrayListOf(
                        R.string.relation_option_1,
                        R.string.relation_option_2,
                        R.string.relation_option_3,
                        R.string.relation_option_4,
                        R.string.relation_option_5
                    ),
                    null,
                    SharedPrefrenceManager.getRelationShipStatus(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.height_question,
                    false,
                    null,
                    R.string.height_placeholder,
                    SharedPrefrenceManager.getUserHeight(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.gym_question,
                    true,
                    arrayListOf(
                        R.string.gym_option_1,
                        R.string.gym_option_2,
                        R.string.gym_option_3
                    ),
                    null,
                    SharedPrefrenceManager.getUserGym(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.edutcation_question,
                    true,
                    arrayListOf(
                        R.string.education_option_1,
                        R.string.education_option_2,
                        R.string.education_option_3,
                        R.string.education_option_4,
                        R.string.education_option_5,
                        R.string.education_option_6
                    ),
                    null,
                    SharedPrefrenceManager.getUserEducationLevel(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.drink_question,
                    true,
                    arrayListOf(
                        R.string.drink_option_1,
                        R.string.drink_option_2,
                        R.string.drink_option_3
                    ),
                    null,
                    SharedPrefrenceManager.getUserDrink(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.smoke_question,
                    true,
                    arrayListOf(
                        R.string.smoke_option_1,
                        R.string.smoke_option_2,
                        R.string.smoke_option_3
                    ),
                    null,
                    SharedPrefrenceManager.getSomke(app.applicationContext)
                )
            )

            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.pets_question,
                    true,
                    arrayListOf(
                        R.string.pet_option_1,
                        R.string.pet_option_2,
                        R.string.pet_option_3,
                        R.string.pet_option_4,
                        R.string.pet_option_5,
                        R.string.pet_option_6
                    ),
                    null,
                    SharedPrefrenceManager.getPets(app.applicationContext)
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.looking_for_question,
                    true,
                    arrayListOf(
                        R.string.looking_for_option_1,
                        R.string.looking_for_option_2,
                        R.string.looking_for_option_3,
                        R.string.looking_for_option_4
                    ),
                    null,
                    SharedPrefrenceManager.getLookingFor(app.applicationContext)
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.kids_question,
                    true,
                    arrayListOf(
                        R.string.kids_option_1,
                        R.string.kids_option_2,
                        R.string.kids_option_3,
                        R.string.kids_option_4
                    ),
                    null,
                    SharedPrefrenceManager.getKids(app.applicationContext)
                )
            )




            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.zodiac_question,
                    true,
                    arrayListOf(
                        R.string.zodiac_option_1,
                        R.string.zodiac_option_2,
                        R.string.zodiac_option_3,
                        R.string.zodiac_option_4,
                        R.string.zodiac_option_5,
                        R.string.zodiac_option_6,
                        R.string.zodiac_option_7,
                        R.string.zodiac_option_8,
                        R.string.zodiac_option_9,
                        R.string.zodiac_option_10,
                        R.string.zodiac_option_11,
                        R.string.zodiac_option_12
                    ),
                    null,
                    SharedPrefrenceManager.getZodiac(app.applicationContext)
                )
            )


            questionModel.add(
                UserBasicInfoQuestionModel(
                    R.string.religion_question,
                    true,
                    arrayListOf(
                        R.string.religin_option_1,
                        R.string.religin_option_2,
                        R.string.religin_option_3,
                        R.string.religin_option_4,
                        R.string.religin_option_5,
                        R.string.religin_option_6
                    ),
                    null,
                    SharedPrefrenceManager.getReligion(app.applicationContext)
                )
            )

        }

        return questionModel
    }
}