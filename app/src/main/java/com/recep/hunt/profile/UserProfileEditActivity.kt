package com.recep.hunt.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import org.jetbrains.anko.find

class UserProfileEditActivity : BaseActivity(),ProfileBasicInfoTappedListner {

    override fun onItemClicked(position: Int, questionModel: UserBasicInfoQuestionModel, icon:Int) {
        launchActivity<UserProfileEditQuestionActivity>{
            putExtra(questionModelKey,questionModel)
            putExtra(questionImageKey,icon)
        }
    }

    private lateinit var writeAboutYouEditText: EditText
    companion object{
        const val questionModelKey = "questionModelKey"
        const val questionImageKey = "questionImageKey"
    }
    private lateinit var userBasicInfoRecyclerView : RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)
        setScreenTitle(R.string.edit_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }

    private fun init(){
        writeAboutYouEditText = find(R.id.write_about_us_et)
        userBasicInfoRecyclerView = find(R.id.edit_profile_user_basic_details_recyclerView)
        setupRecyclerView()

        save_edit_profile_btn.setOnClickListener {
            updateAboutYou()
        }
        setupFields()
    }
    private fun updateAboutYou(){
        val txt = writeAboutYouEditText.text.toString()
        if(txt.isNotEmpty()){
            SharedPrefrenceManager.setAboutYou(this,txt)
        }
    }
    private fun setupFields(){
        val aboutYou = SharedPrefrenceManager.getAboutYou(this)
        if(aboutYou != "null")
        writeAboutYouEditText.setText(aboutYou)


    }
    private fun setupRecyclerView(){
        userBasicInfoRecyclerView.adapter = adapter
        userBasicInfoRecyclerView.layoutManager = LinearLayoutManager(this)
        addBasicInfoItemViews()
    }

    private fun addBasicInfoItemViews(){
        val basicModel= ArrayList<UserBasicInfoModel>()
        val questionModel = getBasicInfoQuestions()
        basicModel.add(UserBasicInfoModel("Relationship status","Single",R.drawable.relationship_icon,questionModel[0]))
        basicModel.add(UserBasicInfoModel("Height",null,R.drawable.height_icon,questionModel[1]))
        basicModel.add(UserBasicInfoModel("Gym",null,R.drawable.gym_icon,questionModel[2]))
        basicModel.add(UserBasicInfoModel("Education level","Engineering",R.drawable.education_icon,questionModel[3]))
        basicModel.add(UserBasicInfoModel("Drink","Socially",R.drawable.drink_icon,questionModel[4]))
        basicModel.add(UserBasicInfoModel("Smoke",null,R.drawable.smoke_icon,questionModel[5]))
        basicModel.add(UserBasicInfoModel("Pets",null,R.drawable.pets_icon,questionModel[6]))
        basicModel.add(UserBasicInfoModel("Looking for","Dating",R.drawable.looking_for_icon,questionModel[7]))
        basicModel.add(UserBasicInfoModel("Kids","Want someday",R.drawable.kids_icon,questionModel[8]))
        basicModel.add(UserBasicInfoModel("Zodiac","Libra",R.drawable.zodiac_icon,questionModel[9]))
        basicModel.add(UserBasicInfoModel("Religion","Punjabi",R.drawable.religion_icon,questionModel[10]))

        for(model in basicModel){
            adapter.add(ProfileBasicInfoItemView(this,model,this))
        }
    }

    private fun getBasicInfoQuestions():ArrayList<UserBasicInfoQuestionModel>{
        val questionModel = ArrayList<UserBasicInfoQuestionModel>()
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
                R.string.relation_option_1
            )
        )

        questionModel.add(
            UserBasicInfoQuestionModel(
                R.string.height_question,
                false,
                null,
                R.string.height_placeholder,
                null
            )
        )

        questionModel.add(
            UserBasicInfoQuestionModel(
                R.string.gym_question,
                true,
                arrayListOf(R.string.gym_option_1, R.string.gym_option_2, R.string.gym_option_3),
                null,
                null
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
                R.string.education_option_4
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
                R.string.drink_option_2
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
                R.string.smoke_option_2
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
                R.string.pet_option_2
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
                R.string.looking_for_option_3
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
                R.string.looking_for_option_3
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
                R.string.zodiac_option_2
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
                R.string.religin_option_3
            )
        )

        return questionModel
    }

}
