package com.recep.hunt.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.recep.hunt.R
import com.recep.hunt.base.activity.BaseActivity
import com.recep.hunt.base.adapter.BaseAdapter
import com.recep.hunt.base.adapter.BaseViewHolder
import com.recep.hunt.base.adapter.GridSpacingItemDecoration
import com.recep.hunt.constants.Constants
import com.recep.hunt.data.repositories.ProfileRepository
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.model.UserProfile.Data
import com.recep.hunt.model.UserProfile.ImageModel
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.profile.viewmodel.ProfileViewModel
import com.recep.hunt.userDetail.UserDetailActivity
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.profile_basic_item_view.view.*
import kotlinx.android.synthetic.main.profile_gender_item_view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Response

class ProfileActivity : BaseActivity() {

    private val profileViewModel :ProfileViewModel by viewModel()

    private val userImageAdapter: UserImagesAdapter by lazy { UserImagesAdapter() }

    private val userBasicInfoAdapter: UserBasicInfoAdapter by lazy { UserBasicInfoAdapter() }

    private val profileRepository :ProfileRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(profile_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        profile_toolbar.setNavigationOnClickListener { finish() }

        initEventHandler()
        initRecyclerUserBasicInfo()
        initRecyclerUserImages()
        initObservers()

    }


//        getQuestionData()





    private fun initEventHandler() {
        btnViewMyProfileAsOthers.setOnClickListener {
            goToUserDetailsActivity()
        }

        settings_profile.setOnClickListener{
         launchActivity<UserProfileSettingsActivity>()
        }
        edit_profile.setOnClickListener{
            launchActivity<UserProfileEditActivity>()
        }
    }

    private fun initObservers() {
        profileViewModel.apply {
            fetchUserProfileInfo()
            userProfileInfoleLiveDataState.observe(this@ProfileActivity, Observer {
                handleFetchingUserProfileState(it)
            })
        }
    }

    private fun handleFetchingUserProfileState(state: CommonState<Response<UserProfileResponse>>?) {
        when(state){
            CommonState.LoadingShow->showProgressDialog()
            CommonState.LoadingFinished->hideProgressDialog()
            is CommonState.Success->{

                if (!state.data.isSuccessful) {
                       val strErrorJson = state.data.errorBody()?.string()
                    if (Utils.isSessionExpire(this@ProfileActivity, strErrorJson)) {
                        return
                    }
                }else initUserInfoUI(state.data.body()!!.data)

            }
            is CommonState.Error->{


            }
        }
    }

    private fun initUserInfoUI(data: Data) {

        Glide.with(this)
            .load(data.profile_pic)
            .placeholder(R.drawable.account_icon)
            .into(ivUserProfile)

        tvUserName.text = data.first_name

        tvAboutUs.text = data.user_info.about

        tvJobTitle.text=data.user_info.job_title

        tvhometown.text=data.user_info.hometown

        tvShcoolOrUniversity.text=data.user_info.school

        setupSelectedGender(data.gender)

        val basicInfoViewModel = BasicInfoViewModel.getInstace(this.application,profileRepository)

        userBasicInfoAdapter.updateItems(basicInfoViewModel.getData())

        if (data.user_profile_image.size>0) userImageAdapter.updateItems(data.user_profile_image)



    }

    private fun goToUserDetailsActivity() {
        launchActivity<UserDetailActivity>()
    }

    private fun initRecyclerUserImages() {
        recyclerViewUserImages.apply {
            setHasFixedSize(false)
            adapter=userImageAdapter
            layoutManager= GridLayoutManager(this@ProfileActivity,3)
            addItemDecoration(GridSpacingItemDecoration(3, resources.getDimension(R.dimen.dp8).toInt(), true))
        }
    }

    private fun initRecyclerUserBasicInfo() {
        recyclerViewBasicInfo.apply {
            setHasFixedSize(false)
            adapter=userBasicInfoAdapter
            layoutManager= LinearLayoutManager(this@ProfileActivity) }

    }

    inner class UserImagesAdapter: BaseAdapter<ImageModel>(itemLayoutRes = R.layout.item_image){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ImageModel> {
            return UserImageViewHolder(getItemView(parent))
        }

        inner class UserImageViewHolder(view: View) : BaseViewHolder<ImageModel>(view) {
            override fun fillData() {
                itemView.btnDelete.visibility= View.GONE
                item?.let { loadImage(it, itemView.ivProfileItem) }
            }

        }
    }

    inner class UserBasicInfoAdapter: BaseAdapter<UserBasicInfoModel>(itemLayoutRes = R.layout.profile_basic_item_view){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UserBasicInfoModel> {
            return UserBasicInfoViewHolder(getItemView(parent))
        }

        inner class UserBasicInfoViewHolder(view: View) : BaseViewHolder<UserBasicInfoModel>(view) {
            override fun fillData() {
                itemView. basic_info_icon.image = item?.icon?.let { this@ProfileActivity.resources.getDrawable(it) }
                itemView.basic_info_title.text = item?.title
                val questionModel = item?.questions

                    if (questionModel?.selectedValue != Constants.NULL) {
                        itemView.basic_info_add_image.visibility = View.GONE
                        itemView.basic_info_detail.text = questionModel?.selectedValue
                    } else {
                        itemView.basic_info_add_image.visibility = View.VISIBLE
                        itemView.basic_info_detail.text = ""
                    }
                    itemView.setOnClickListener {
                     //   listener?.onItemClicked(position, questionModel, model.icon)
                    }

            }

        }
    }

    fun loadImage(imageModel: ImageModel, imageView: ImageView){
        val multi = MultiTransformation<Bitmap>(
            CropSquareTransformation(),
            RoundedCornersTransformation(1, 8, RoundedCornersTransformation.CornerType.ALL)
        )
        Glide.with(this).load(imageModel.image)
            .apply(RequestOptions.bitmapTransform(multi))
            .into(imageView)

    }

    private fun setupSelectedGender(selectedgender: String) {
        when (selectedgender.toLowerCase()) {
            Constants.MALE.toLowerCase() -> {
                profile_male_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                profile_male_gender_btn.textColor = resources.getColor(R.color.white)

                profile_female_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_female_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)

                profile_other_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_other_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            Constants.FEMALE.toLowerCase() -> {
                profile_female_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                profile_female_gender_btn.textColor = resources.getColor(R.color.white)

                profile_male_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_male_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)

                profile_other_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_other_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            else -> {
                profile_other_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                profile_other_gender_btn.textColor = resources.getColor(R.color.white)

                profile_female_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_female_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)

                profile_male_gender_btn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                profile_male_gender_btn.textColor = resources.getColor(R.color.app_light_text_color)
            }
        }
    }


}
