package com.recep.hunt.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.constraintlayout.solver.widgets.Helper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.Share
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.profile_basic_item_view.view.*
import kotlinx.android.synthetic.main.profile_header_layout_item.view.*
import kotlinx.android.synthetic.main.profile_simple_header_item.view.*
import kotlinx.android.synthetic.main.profile_simple_title_item.view.*
import kotlinx.android.synthetic.main.six_photos_item_layout.view.*
import org.jetbrains.anko.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        init()
    }

    private fun init() {
        recyclerView = find(R.id.profile_recyclerView)
        setSupportActionBar(profile_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        setupRecyclerView()
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@UserProfileActivity)

        addProfileHeader()
        addSixPhotosItemView()
        addHomeTownAndSchoolItemView()
        addUserGenderItemView()
        adapter.add(ProfileHeaderTitle(resources.getString(R.string.basic_info)))
        addBasicInfoItemViews()


    }

    private fun addProfileHeader() {
        adapter.add(ProfileHeaderView(this))
    }

    private fun addSixPhotosItemView() {
        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)

        if(firstImage != Constants.NULL || secondImage != Constants.NULL || thirdImage != Constants.NULL || fourthImage != Constants.NULL || fifthImage != Constants.NULL || sixthImage != Constants.NULL)
            adapter.add(ProfileSixPhotosView(this))
    }

    private fun addHomeTownAndSchoolItemView() {
        val homeTown = SharedPrefrenceManager.getHomeTown(this)
        val school = SharedPrefrenceManager.getSchoolUniversity(this)

        if(homeTown != Constants.NULL){
            adapter.add(ProfileHeaderTitle(resources.getString(R.string.hometown)))
            adapter.add(ProfileSimpleItem(homeTown))
        }

        if(school != Constants.NULL){
            adapter.add(ProfileHeaderTitle(resources.getString(R.string.schooloruniversity)))
            adapter.add(ProfileSimpleItem(school))
        }

        adapter.add(ProfileHeaderTitle(resources.getString(R.string.iam)))
    }

    private fun addUserGenderItemView() {
        adapter.add(ProfileGenderItemView(this, SharedPrefrenceManager.getUserGender(this), false))
    }

    private fun addBasicInfoItemViews() {
        val basicModel = ArrayList<UserBasicInfoModel>()
        val questionModel = getBasicInfoQuestions()
        basicModel.add(UserBasicInfoModel("Relationship status",SharedPrefrenceManager.getRelationShipStatus(this),R.drawable.relationship_icon,questionModel[0]))
        basicModel.add(UserBasicInfoModel("Height", SharedPrefrenceManager.getUserHeight(this), R.drawable.height_icon, questionModel[1]))
        basicModel.add(UserBasicInfoModel("Gym", SharedPrefrenceManager.getUserGym(this), R.drawable.gym_icon, questionModel[2]))
        basicModel.add(UserBasicInfoModel("Education level", SharedPrefrenceManager.getUserEducationLevel(this), R.drawable.education_icon,questionModel[3]))
        basicModel.add(UserBasicInfoModel("Drink", SharedPrefrenceManager.getUserDrink(this), R.drawable.drink_icon, questionModel[4]))
        basicModel.add(UserBasicInfoModel("Smoke", SharedPrefrenceManager.getSomke(this), R.drawable.smoke_icon, questionModel[5]))
        basicModel.add(UserBasicInfoModel("Pets",SharedPrefrenceManager.getPets(this), R.drawable.pets_icon, questionModel[6]))
        basicModel.add(UserBasicInfoModel("Looking for", SharedPrefrenceManager.getLookingFor(this), R.drawable.looking_for_icon, questionModel[7]))
        basicModel.add(UserBasicInfoModel("Kids", SharedPrefrenceManager.getKids(this), R.drawable.kids_icon, questionModel[8]))
        basicModel.add(UserBasicInfoModel("Zodiac", SharedPrefrenceManager.getZodiac(this), R.drawable.zodiac_icon, questionModel[9]))
        basicModel.add(UserBasicInfoModel("Religion", SharedPrefrenceManager.getReligion(this), R.drawable.religion_icon, questionModel[10]))

        for (model in basicModel) {
            adapter.add(ProfileBasicInfoItemView(this, model, null))
        }

    }

    private fun getBasicInfoQuestions(): ArrayList<UserBasicInfoQuestionModel> {
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
                SharedPrefrenceManager.getRelationShipStatus(this)
            )
        )

        questionModel.add(
            UserBasicInfoQuestionModel(
                R.string.height_question,
                false,
                null,
                R.string.height_placeholder,
                SharedPrefrenceManager.getUserHeight(this)
            )
        )

        questionModel.add(
            UserBasicInfoQuestionModel(
                R.string.gym_question,
                true,
                arrayListOf(R.string.gym_option_1, R.string.gym_option_2, R.string.gym_option_3),
                null,
                SharedPrefrenceManager.getUserGym(this)
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
                SharedPrefrenceManager.getUserEducationLevel(this)
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
                SharedPrefrenceManager.getUserDrink(this)
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
                SharedPrefrenceManager.getSomke(this)
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
                SharedPrefrenceManager.getPets(this)
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
                SharedPrefrenceManager.getLookingFor(this)
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
                SharedPrefrenceManager.getKids(this)
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
                SharedPrefrenceManager.getZodiac(this)
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
                SharedPrefrenceManager.getReligion(this)
            )
        )

        return questionModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.edit_profile -> launchActivity<UserProfileEditActivity>()
                R.id.settings_profile -> launchActivity<UserProfileSettingsActivity>()
                else -> finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}

//ProfileHeader
class ProfileHeaderView(private val context: Context) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.profile_header_layout_item

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val userName = SharedPrefrenceManager.getUserFirstName(context)
        val userJobtitle = SharedPrefrenceManager.getJobTitle(context)
        val aboutYou = SharedPrefrenceManager.getAboutYou(context)
        val userImage = SharedPrefrenceManager.getUserImage(context)

        Picasso.get().load(userImage)
            .transform(Helpers.getPicassoTransformation(viewHolder.itemView.profile_header_user_image))
            .into(viewHolder.itemView.profile_header_user_image)


        viewHolder.itemView.profile_header_user_name.text = userName
        if(aboutYou != Constants.NULL)
        viewHolder.itemView.user_aboutus.text = aboutYou
        if(userJobtitle != Constants.NULL)
        viewHolder.itemView.user_job_title_tv.text = userJobtitle

    }
}

//Profile User Images - 6
class ProfileSixPhotosView(private val context: Context) : Item<ViewHolder>() {
    var bitmap: Bitmap? = null
    override fun getLayout() = R.layout.six_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_image_1.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getFirstImg(context)))
        viewHolder.itemView.user_image_2.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getSecImg(context)))
        viewHolder.itemView.user_image_3.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getThirdImg(context)))
        viewHolder.itemView.user_image_4.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getFourthImg(context)))
        viewHolder.itemView.user_image_5.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getFiveImg(context)))
        viewHolder.itemView.user_image_6.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getSixImg(context)))
    }

    fun StringToBitmap(img: String): Bitmap? {
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size);

        }
        return bitmap
    }

}

//Profile Simple title Header
class ProfileHeaderTitle(private val txt: String) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.profile_simple_header_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.profile_simple_header_title.text = txt
    }
}

//Profile Simple detail item view
class ProfileSimpleItem(private val detail: String) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.profile_simple_title_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.profile_simple_item_title.text = detail
    }
}

//PRofile Gender View
class ProfileGenderItemView(private val context: Context, private val gender: String, private val isEditMode: Boolean) :
    Item<ViewHolder>() {
    private lateinit var maleBtn: Button
    private lateinit var femaleBtn: Button
    private lateinit var otherBtn: Button
    override fun getLayout() = R.layout.profile_gender_item_view
    override fun bind(viewHolder: ViewHolder, position: Int) {
        maleBtn = viewHolder.itemView.find(R.id.profile_male_gender_btn)
        femaleBtn = viewHolder.itemView.find(R.id.profile_female_gender_btn)
        otherBtn = viewHolder.itemView.find(R.id.profile_other_gender_btn)

        if (isEditMode) {
            maleBtn.isEnabled = true
            femaleBtn.isEnabled = true
            otherBtn.isEnabled = true
        } else {
            maleBtn.isEnabled = false
            femaleBtn.isEnabled = false
            otherBtn.isEnabled = false
        }

        maleBtn.setOnClickListener {
            setupSelectedGender(Constants.MALE)
        }

        femaleBtn.setOnClickListener {
            setupSelectedGender(Constants.FEMALE)
        }

        otherBtn.setOnClickListener {
            setupSelectedGender(Constants.OTHERS)
        }

        setupSelectedGender(SharedPrefrenceManager.getUserGender(context))


    }

    private fun setupSelectedGender(selectedgender: String) {
        when (selectedgender) {
            Constants.MALE -> {
                maleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.white)

                femaleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                otherBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.app_light_text_color)
            }
            Constants.FEMALE -> {
                femaleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                femaleBtn.textColor = context.resources.getColor(R.color.white)

                maleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                otherBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.app_light_text_color)
            }
            else -> {
                otherBtn.background = context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.white)

                maleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                femaleBtn.background = context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)
            }
        }
    }
}

//Basic Info
class ProfileBasicInfoItemView(
    private val context: Context,
    private val model: UserBasicInfoModel,
    val listener: ProfileBasicInfoTappedListner?
) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.profile_basic_item_view

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.basic_info_icon.image = context.resources.getDrawable(model.icon)
        viewHolder.itemView.basic_info_title.text = model.title
        val questionModel = model.questions

        if (questionModel.selectedValue != Constants.NULL) {
            viewHolder.itemView.basic_info_add_image.visibility = View.GONE
            viewHolder.itemView.basic_info_detail.text = questionModel.selectedValue
        } else {
            viewHolder.itemView.basic_info_add_image.visibility = View.VISIBLE
            viewHolder.itemView.basic_info_detail.text = ""
        }
        viewHolder.itemView.setOnClickListener {
            listener?.onItemClicked(position, questionModel, model.icon)
        }


    }
}