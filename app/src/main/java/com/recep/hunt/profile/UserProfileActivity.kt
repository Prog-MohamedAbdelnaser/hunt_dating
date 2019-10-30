package com.recep.hunt.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.constraintlayout.solver.widgets.Helper
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
/*import com.facebook.share.Share*/
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants
import com.recep.hunt.model.UserProfile.UserInfoModel
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.userDetail.UserDetailActivity
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class UserProfileActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var userInfo : UserInfoModel
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
        getData()
    }

    fun getData(){
        val call = ApiClient.getClient.getUserProfile()

        call.enqueue(object:Callback<UserProfileResponse>{
            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                userInfo= response.body()!!.data.user_info
                setupRecyclerView()

            }

        })

    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
//        setupRecyclerView()
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

        if (firstImage != Constants.NULL || secondImage != Constants.NULL || thirdImage != Constants.NULL || fourthImage != Constants.NULL || fifthImage != Constants.NULL || sixthImage != Constants.NULL)
            adapter.add(ProfileSixPhotosView(this))
    }

    private fun addHomeTownAndSchoolItemView() {
        val homeTown = SharedPrefrenceManager.getHomeTown(this)
        val school = SharedPrefrenceManager.getSchoolUniversity(this)

        if (homeTown != Constants.NULL) {
            adapter.add(ProfileHeaderTitle(resources.getString(R.string.hometown)))
            adapter.add(ProfileSimpleItem(homeTown))
        }

        if (school != Constants.NULL) {
            adapter.add(ProfileHeaderTitle(resources.getString(R.string.schooloruniversity)))
            adapter.add(ProfileSimpleItem(school))
        }

        adapter.add(ProfileHeaderTitle(resources.getString(R.string.iam)))
    }

    private fun addUserGenderItemView() {
        adapter.add(ProfileGenderItemView(this, SharedPrefrenceManager.getUserGender(this), false))
    }

    private fun addBasicInfoItemViews() {
//        val basicInfoViewModel = ViewModelProviders.of(this).get(BasicInfoViewModel::class.java)
        val basicInfoViewModel = BasicInfoViewModel.getInstace(this.application)
        val basicModel = basicInfoViewModel.getData()

        for (model in basicModel) {
            adapter.add(ProfileBasicInfoItemView(this, model, null))
        }

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
    var bitmap: Bitmap? = null
    override fun getLayout() = R.layout.profile_header_layout_item

    override fun bind(viewHolder: ViewHolder, position: Int) {


        val userName = SharedPrefrenceManager.getUserFirstName(context)
        val userJobtitle = SharedPrefrenceManager.getJobTitle(context)
        val aboutYou = SharedPrefrenceManager.getAboutYou(context)
        val userImage = SharedPrefrenceManager.getProfileImg(context)
        val socialType = SharedPrefrenceManager.getsocialType(context)

        if (socialType.equals("social")) {
//            Picasso.get().load(userImage)
//                .transform(Helpers.getPicassoTransformation(viewHolder.itemView.profile_header_user_image))
//                .into(viewHolder.itemView.profile_header_user_image)
            Picasso.get().load(userImage).placeholder(R.drawable.account_icon)
                .into(viewHolder.itemView.profile_header_user_image)
        } else {
            viewHolder.itemView.profile_header_user_image.setImageBitmap(StringToBitmap(userImage))
        }

        viewHolder.itemView.profile_header_user_name.text = userName
        if (aboutYou != Constants.NULL)
            viewHolder.itemView.user_aboutus.text = aboutYou
        if (userJobtitle != Constants.NULL)
            viewHolder.itemView.user_job_title_tv.text = userJobtitle

        viewHolder.itemView.view_my_profile_as_others_btn.setOnClickListener {
            context.launchActivity<UserDetailActivity>()
        }

    }

    fun StringToBitmap(img: String): Bitmap? {
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size);

        }
        return bitmap
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