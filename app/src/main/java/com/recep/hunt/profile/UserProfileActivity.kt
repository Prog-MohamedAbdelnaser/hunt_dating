package com.recep.hunt.profile

/*import com.facebook.share.Share*/
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.model.UserProfile.Data
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.userDetail.UserDetailActivity
import com.recep.hunt.utilis.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.profile_basic_item_view.view.*
import kotlinx.android.synthetic.main.profile_header_layout_item.view.*
import kotlinx.android.synthetic.main.profile_simple_header_item.view.*
import kotlinx.android.synthetic.main.profile_simple_title_item.view.*
import kotlinx.android.synthetic.main.six_photos_item_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserProfileActivity"

class UserProfileActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var recyclerView: RecyclerView
    lateinit var userInfo: Data
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
//        getData()
        profile_toolbar.setNavigationOnClickListener { finish() }
        settings_profile.setOnClickListener(this)
        edit_profile.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
       p0?.let {
           when (it.id) {
               R.id.edit_profile -> launchActivity<UserProfileEditActivity>()
               R.id.settings_profile -> launchActivity<UserProfileSettingsActivity>()
               else -> finish()

           }
       }

    }

    private fun getData() {
        val progressDialog = Helpers.showDialog(this,this,"Getting UserProfile")
        val call =
            ApiClient.getClient.getUserProfile(SharedPrefrenceManager.getUserToken(this@UserProfileActivity))
        progressDialog.show()
        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure")
                setupRecyclerView()
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {

                progressDialog.dismiss()
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@UserProfileActivity, strErrorJson)) {
                        return
                    }
                }

                if(response.isSuccessful) {
                    response.body()?.let {
                        Log.d(TAG, "response body = $it")
                        userInfo = it.data
                    }
                    setPrefData()
                }
                else {
                    Log.e(TAG, "else ${response.errorBody().toString()}")
                }

                setupRecyclerView()

            }

        })

    }


    private fun setPrefData() {

        try {
            userInfo.let {
                SharedPrefrenceManager.setUserFirstName(this, userInfo.first_name)
                SharedPrefrenceManager.setUserLastName(this, userInfo.last_name)
                SharedPrefrenceManager.setUserMobileNumber(this, userInfo.mobile_no)
                SharedPrefrenceManager.setUserCountry(this, userInfo.country)
                SharedPrefrenceManager.setUserCountryCode(this, userInfo.country_code)
                SharedPrefrenceManager.setUserGender(this, userInfo.gender)
                SharedPrefrenceManager.setUserEmail(this, userInfo.email)
                SharedPrefrenceManager.setUserLatitude(this, userInfo.lat)
                SharedPrefrenceManager.setProfileImg(this, userInfo.profile_pic)


                for ((index, it1) in userInfo.user_profile_image.withIndex()) {
                    when (index) {
                        0 -> {
                            SharedPrefrenceManager.setFirstImg(this, it1.image)
                        }
                        1 -> {
                            SharedPrefrenceManager.setSecImg(this, it1.image)
                        }
                        2 -> {
                            SharedPrefrenceManager.setThirdImg(this, it1.image)
                        }
                        3 -> {
                            SharedPrefrenceManager.setFourthImg(this, it1.image)
                        }
                        4 -> {
                            SharedPrefrenceManager.setFiveImg(this, it1.image)
                        }
                        5 -> {
                            SharedPrefrenceManager.setSixImg(this, it1.image)
                        }
                    }
                }

                SharedPrefrenceManager.setAboutYou(this,userInfo.user_info.about)
                SharedPrefrenceManager.setJobTitle(this,userInfo.user_info.job_title)
                SharedPrefrenceManager.setCompanyName(this,userInfo.user_info.company)
                SharedPrefrenceManager.setHomeTown(this,userInfo.user_info.hometown)
                SharedPrefrenceManager.setSchoolUniversity(this,userInfo.user_info.school)
                SharedPrefrenceManager.setUserHeight(this,userInfo.user_info.height)
                SharedPrefrenceManager.setUserGym(this,userInfo.user_info.gym)
                SharedPrefrenceManager.setUserEducationLevel(this,userInfo.user_info.education_level)
                SharedPrefrenceManager.setUserDrink(this,userInfo.user_info.drink)
                SharedPrefrenceManager.setSmoke(this,userInfo.user_info.smoke)
                SharedPrefrenceManager.setPets(this,userInfo.user_info.pets)
                SharedPrefrenceManager.setKids(this,userInfo.user_info.kids)
                SharedPrefrenceManager.setZodiac(this,userInfo.user_info.zodiac)
                SharedPrefrenceManager.setReligion(this,userInfo.user_info.religion)


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onResume() {
        super.onResume()
//        adapter.clear()
//        setupRecyclerView()
//        adapter.notifyDataSetChanged()
        getData()
    }

    private fun setupRecyclerView() {
        adapter = GroupAdapter<ViewHolder>()
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.profile_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item != null) {
//            when (item.itemId) {
//                R.id.edit_profile -> launchActivity<UserProfileEditActivity>()
//                R.id.settings_profile -> launchActivity<UserProfileSettingsActivity>()
//                else -> finish()
//
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
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
            Glide.with(context)
                .load(userImage)
                .placeholder(R.drawable.account_icon)
                .into(viewHolder.itemView.profile_header_user_image)
        } else {
            Glide.with(context)
                .load(userImage)
                .placeholder(R.drawable.account_icon)
                .into(viewHolder.itemView.profile_header_user_image)
//            viewHolder.itemView.profile_header_user_image.setImageBitmap(StringToBitmap(userImage))
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
            var b = Base64.decode(img, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)

        }
        return bitmap
    }
}

//Profile User Images - 6
class ProfileSixPhotosView(private val context: Context) : Item<ViewHolder>() {
    var bitmap: Bitmap? = null
    override fun getLayout() = R.layout.six_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val firstImage = SharedPrefrenceManager.getFirstImg(context)
        if (firstImage.contains("http")) {
            Glide.with(context)
                .load(firstImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_1)
        } else {
            viewHolder.itemView.user_image_1.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getFirstImg(
                        context
                    )
                )
            )
        }

        val secondImage = SharedPrefrenceManager.getSecImg(context)
        if (secondImage.contains("http")) {
            Glide.with(context)
                .load(secondImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_2)
        } else {
            viewHolder.itemView.user_image_2.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getSecImg(
                        context
                    )
                )
            )
        }

        val thirdImage = SharedPrefrenceManager.getThirdImg(context)
        if (thirdImage.contains("http")) {
            Glide.with(context)
                .load(thirdImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_3)
        } else {
            viewHolder.itemView.user_image_3.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getThirdImg(
                        context
                    )
                )
            )
        }


        val fourthImage = SharedPrefrenceManager.getFourthImg(context)
        if (fourthImage.contains("http")) {
            Glide.with(context)
                .load(fourthImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_4)
        } else {
            viewHolder.itemView.user_image_4.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getFourthImg(
                        context
                    )
                )
            )
        }


        val fiveImage = SharedPrefrenceManager.getFiveImg(context)
        if (fiveImage.contains("http")) {
            Glide.with(context)
                .load(fiveImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_5)
        } else {
            viewHolder.itemView.user_image_5.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getFiveImg(
                        context
                    )
                )
            )
        }


        val sixImage = SharedPrefrenceManager.getSixImg(context)
        if (sixImage.contains("http")) {
            Glide.with(context)
                .load(sixImage)
                .placeholder(R.drawable.add_image)
                .into(viewHolder.itemView.user_image_6)
        } else {
            viewHolder.itemView.user_image_6.setImageBitmap(
                StringToBitmap(
                    SharedPrefrenceManager.getSixImg(
                        context
                    )
                )
            )
        }


    }

    fun StringToBitmap(img: String): Bitmap? {
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
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
class ProfileGenderItemView(
    private val context: Context,
    private val gender: String,
    private val isEditMode: Boolean
) :
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
        when (selectedgender.toLowerCase()) {
            Constants.MALE.toLowerCase() -> {
                maleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.white)

                femaleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                otherBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.app_light_text_color)
            }
            Constants.FEMALE.toLowerCase() -> {
                femaleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                femaleBtn.textColor = context.resources.getColor(R.color.white)

                maleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                otherBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.app_light_text_color)
            }
            else -> {
                otherBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_selected_btn)
                otherBtn.textColor = context.resources.getColor(R.color.white)

                maleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleBtn.textColor = context.resources.getColor(R.color.app_light_text_color)

                femaleBtn.background =
                    context.resources.getDrawable(R.drawable.profile_gender_unselected_btn)
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