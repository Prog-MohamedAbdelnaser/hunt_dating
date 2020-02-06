package com.recep.hunt.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.overrideOf
import com.bumptech.glide.request.target.Target
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.base.adapter.BaseAdapter
import com.recep.hunt.base.adapter.BaseViewHolder
import com.recep.hunt.base.adapter.GridSpacingItemDecoration
import com.recep.hunt.common.AskBeforeMakeActionDialog
import com.recep.hunt.constants.Constants
import com.recep.hunt.data.sources.local.AppPreference
import com.recep.hunt.model.UpdateUserInfoResponse.UpdateUserInfoResponseModel
import com.recep.hunt.model.UserProfile.ImageModel
import com.recep.hunt.model.UserProfile.ImagesListModel
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.profile.model.UserImage
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.setupProfile.SetupProfileUploadPhotoStep2Activity
import com.recep.hunt.userDetail.StringToBitmap
import com.recep.hunt.utilis.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_match_questionnaire.*
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.six_photos_item_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class UserProfileEditActivity : BaseActivity(), ProfileBasicInfoTappedListner {


    private lateinit var writeAboutYouEditText: EditText
    private lateinit var jobTitleEditText: EditText
    private lateinit var companyNameEditText: EditText
    private lateinit var homeTownEditText: EditText
    private lateinit var schoolOrUniversityEditText: EditText
    private lateinit var ivPencilEditProfileId: ImageView
    private lateinit var ivEditProfielId: CircleImageView
    private lateinit var maleGenderBtn: Button
    private lateinit var femaleGenderBtn: Button
    private lateinit var otherGenderBtn: Button
    private var count = 1
    private var flag = 0
    private lateinit var oldGender: String
    private var bitmap: Bitmap? = null
    private val userImageAdapter:UserImagesAdapter by lazy { UserImagesAdapter() }
    companion object {
        const val questionModelKey = "questionModelKey"
        const val questionImageKey = "questionImageKey"
        const val imgBlock = "imgBlock"
        const val questionPositionKey = "questionPositionKey"
        const val REQUEST_CODE_UPLOAD_IMAGE_INTENT=2222
    }


    private lateinit var userBasicInfoRecyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)
        setScreenTitle(R.string.edit_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE

        initRecyclerUserImages()
        init()
    }

    private fun init() {
        ivPencilEditProfileId = find(R.id.ivPencilEditProfileId)
        ivEditProfielId = find(R.id.ivEditProfielId)
        writeAboutYouEditText = find(R.id.write_about_us_et)
        userBasicInfoRecyclerView = find(R.id.edit_profile_user_basic_details_recyclerView)
        jobTitleEditText = find(R.id.add_job_title_et)
        companyNameEditText = find(R.id.add_company_name_et)
        homeTownEditText = find(R.id.add_hometown_et)
        schoolOrUniversityEditText = find(R.id.add_school_or_university_et)

        maleGenderBtn = find(R.id.edit_profile_male_gender_btn)
        femaleGenderBtn = find(R.id.edit_profile_female_gender_btn)
        otherGenderBtn = find(R.id.edit_profile_other_gender_btn)

        about_header.text =
            resources.getString(R.string.about_you, SharedPrefrenceManager.getUserFirstName(this))
        val dialog = Helpers.showDialog(this, this, "Updating Profile")
        oldGender = SharedPrefrenceManager.getUserGender(this)

        save_edit_profile_btn.setOnClickListener {
            Log.i("save_edit_profile_btn","setOnClickListener ")

            dialog.show()
            updateAboutYou()
            updateJobTitle()
            updateCompanyName()
            updateHomeTown()
            updateSchool()
            updateGenderPrefrence()

//            val userModel = UpdateUserInfoModel(
//                SharedPrefrenceManager.getAboutYou(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getJobTitle(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getCompanyName(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getHomeTown(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getSchoolUniversity(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getUserHeight(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getUserGym(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getUserEducationLevel(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getUserDrink(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getSomke(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getPets(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getKids(this@UserProfileEditActivity),
//                SharedPrefrenceManager.getZodiac(this@UserProfileEditActivity),
//                "",
//                SharedPrefrenceManager.getReligion(this@UserProfileEditActivity),
//                "",
//                SharedPrefrenceManager.getUserGender(this@UserProfileEditActivity)
//            )

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart(
                "about",
                SharedPrefrenceManager.getAboutYou(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "job_title",
                SharedPrefrenceManager.getJobTitle(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "company",
                SharedPrefrenceManager.getCompanyName(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "hometown",
                SharedPrefrenceManager.getHomeTown(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "school",
                SharedPrefrenceManager.getSchoolUniversity(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "height",
                SharedPrefrenceManager.getUserHeight(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "gym",
                SharedPrefrenceManager.getUserGym(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "education_level",
                SharedPrefrenceManager.getUserEducationLevel(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "drink",
                SharedPrefrenceManager.getUserDrink(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "smoke",
                SharedPrefrenceManager.getSomke(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "pets",
                SharedPrefrenceManager.getPets(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "kids",
                SharedPrefrenceManager.getKids(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "zodiac",
                SharedPrefrenceManager.getZodiac(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "religion",
                SharedPrefrenceManager.getReligion(this@UserProfileEditActivity)
            )
            builder.addFormDataPart(
                "gender",
                SharedPrefrenceManager.getUserGender(this@UserProfileEditActivity)
            )

            val call = ApiClient.getClient.saveUserDetails(
                builder.build(),
                SharedPrefrenceManager.getUserToken(this)
            )
            call.enqueue(object :
                Callback<UpdateUserInfoResponseModel> {
                override fun onFailure(call: Call<UpdateUserInfoResponseModel>, t: Throwable) {
                    Toast.makeText(
                        this@UserProfileEditActivity,
                        "Something want wrong,Please Try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<UpdateUserInfoResponseModel>,
                    response: Response<UpdateUserInfoResponseModel>
                ) {

                    dialog.dismiss()
                    if (!response.isSuccessful && !isFinishing) {
                        val strErrorJson = response.errorBody()?.string()
                        if (Utils.isSessionExpire(this@UserProfileEditActivity, strErrorJson)) {
                            return
                        }
                    }

                    Log.i("save_edit_profile_btn","response${response.isSuccessful} ")

                    if (response.isSuccessful) {
                        saveUserProfile()
                        saveImages()
                    }

                }
            })

//            Run.after(2000) {
//                dialog.dismiss()
//                finish()
//
//            }

        }


        //bindSixImages()
        setupFields()
        setupRecyclerView()
        Log.i("sharePrefeImageSetup","firstImage ")

        sharePrefeImageSetup()
        setupChangeGenderBtn()
        setupSelectedGender(SharedPrefrenceManager.getUserGender(this))

        ivPencilEditProfileId.setOnClickListener()
        {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false).start()
        }

    }

    override fun onItemClicked(
        position: Int,
        questionModel: UserBasicInfoQuestionModel,
        icon: Int
    ) {
        launchActivity<UserProfileEditQuestionActivity> {
            putExtra(questionModelKey, questionModel)
            putExtra(questionPositionKey, position)
            putExtra(questionImageKey, icon)
        }
    }


    private fun saveUserProfile() {
        val profileImage = SharedPrefrenceManager.getProfileImg(this)
        val dialog = Helpers.showDialog(this, this, "Updating Images")
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        var isImageAvailable = false
        if (!TextUtils.isEmpty(profileImage)) {
            val firstFile = getFiles(profileImage, 0)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (isImageAvailable) {
            val call = ApiClient.getClient.saveImages(
                builder.build(),
                SharedPrefrenceManager.getUserToken(this)
            )
            call.enqueue(object :
                Callback<UpdateUserInfoResponseModel> {
                override fun onFailure(call: Call<UpdateUserInfoResponseModel>, t: Throwable) {
                    Toast.makeText(
                        this@UserProfileEditActivity,
                        "Something want wrong,Please Try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<UpdateUserInfoResponseModel>,
                    response: Response<UpdateUserInfoResponseModel>
                ) {
                    dialog.dismiss()
                    if (!response.isSuccessful && !isFinishing) {
                        val strErrorJson = response.errorBody()?.string()
                        if (Utils.isSessionExpire(this@UserProfileEditActivity, strErrorJson)) {
                            return
                        }
                    }


                    if (response.isSuccessful) {
                        finish()
                    }
//                    dialog.dismiss()
//                    finish()
                }
            })
        }

    }


    private fun saveImages() {

        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)
        Log.i("SixImage","${sixthImage}")
        val dialog = Helpers.showDialog(this, this, "Updating Images")
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        var isImageAvailable = false



        if (!TextUtils.isEmpty(firstImage)) {
            val firstFile = getFiles(firstImage, 1)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (!TextUtils.isEmpty(secondImage)) {
            val firstFile = getFiles(secondImage, 2)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (!TextUtils.isEmpty(thirdImage)) {
            val firstFile = getFiles(thirdImage, 3)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (!TextUtils.isEmpty(fourthImage)) {
            val firstFile = getFiles(fourthImage, 4)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (!TextUtils.isEmpty(fifthImage)) {
            val firstFile = getFiles(fifthImage, 5)
            if (firstFile.exists() && firstFile.length() > 0) {
                builder.addFormDataPart(
                    "user_profile",
                    firstFile.name,
                    RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile)
                )
                isImageAvailable = true
            }
        }

        if (!TextUtils.isEmpty(sixthImage)) {
            val firstFile = getFiles(sixthImage, 6)
            if (firstFile.exists() && firstFile.length() > 0) {

                Log.i("getFiles","try add")
                builder.addFormDataPart("user_profile", firstFile.name, RequestBody.create(MediaType.parse("multipart/form-questionData"), firstFile))
                isImageAvailable = true

            }
        }

        if (isImageAvailable) {
            val call = ApiClient.getClient.addImageInAlbum(
                builder.build(),
                SharedPrefrenceManager.getUserToken(this)
            )
            call.enqueue(object :
                Callback<UpdateUserInfoResponseModel> {
                override fun onFailure(call: Call<UpdateUserInfoResponseModel>, t: Throwable) {
                    Toast.makeText(
                        this@UserProfileEditActivity,
                        "Something want wrong,Please Try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<UpdateUserInfoResponseModel>,
                    response: Response<UpdateUserInfoResponseModel>
                ) {
                    dialog.dismiss()
                    if (!response.isSuccessful && !isFinishing) {
                        val strErrorJson = response.errorBody()?.string()
                        if (Utils.isSessionExpire(this@UserProfileEditActivity, strErrorJson)) {
                            return
                        }
                    }


                    if (response.isSuccessful) {
                        finish()
                    }
//                    dialog.dismiss()
//                    finish()
                }
            })
        }

    }


    private fun initRecyclerUserImages() {
        recyclerViewUserImages.apply {
            setHasFixedSize(false)
            adapter=userImageAdapter
            layoutManager= GridLayoutManager(this@UserProfileEditActivity,3)
            addItemDecoration(GridSpacingItemDecoration(3, resources.getDimension(R.dimen.dp8).toInt(), true))

        }



    }

    private fun getFiles(strImage: String, position: Int): File {
        val mFile = File(cacheDir, "image_$position.jpeg")
        mFile.createNewFile()

//Convert bitmap to byte array
        val bitmap = StringToBitmap(strImage)
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 60, bos)
        val bitmapData = bos.toByteArray()

//write the bytes in file
        val fos = FileOutputStream(mFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return mFile
    }

    private fun updateGenderPrefrence() {
        var gender = SharedPrefrenceManager.getUserGender(this)
        if (!gender.equals(oldGender)) {
            SharedPrefrenceManager.setUserGenderChanged(this, false)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageFile: File
        Log.i("onActivityResult","requestCode$requestCode")
        if (requestCode === Config.RC_PICK_IMAGES && resultCode === Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            if (images.size == 1) {
                imageFile = File(images[0].path)
                MediaScannerConnection.scanFile(
                    this, arrayOf(imageFile.absolutePath), null
                ) { path, uri ->
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).start(this)
                }

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                SharedPrefrenceManager.setProfileImg(
                    this@UserProfileEditActivity,
                    bitMapToString(bitmap)
                )
                SharedPrefrenceManager.setsocialType(this@UserProfileEditActivity, "none")
                ivEditProfielId.setImageBitmap(bitmap)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun sharePrefeImageSetup() {

    /*    val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)*/
        val socialTypeStr = SharedPrefrenceManager.getsocialType(this)

        if (socialTypeStr.equals("social")) {
            Glide.with(this)
                .load(Uri.parse(SharedPrefrenceManager.getProfileImg(this)))
                .placeholder(R.drawable.account_icon).into(ivEditProfielId)
        } else {
            val profile = SharedPrefrenceManager.getProfileImg(this)

            if (profile.contains("http")) {
                Glide.with(this)
                    .load(profile)
                    .placeholder(R.drawable.account_icon)
                    .into(ivEditProfielId)
            } else {
                ivEditProfielId.setImageBitmap(StringToBitmap(profile))
            }

        }
        val appPreference: AppPreference by inject()

       val list = appPreference.getObject("IMAGES_MODULE", ImagesListModel::class.java)

        Log.i("ImageListnow ","${list!!.toList()[0].toString()}")
        userImageAdapter.updateItems(list!!.toList())

/*
        Log.i("ProfileImage","firstImage $firstImage ")
        Log.i("HTTPCHECK","${firstImage.contains("http")}")

            userImageAdapter.addItem(UserImage(1,firstImage))

            userImageAdapter.addItem(UserImage(2,secondImage))

            userImageAdapter.addItem(UserImage(3,thirdImage))

            userImageAdapter.addItem(UserImage(4,fourthImage))

            userImageAdapter.addItem(UserImage(5,fifthImage))

            userImageAdapter.addItem(UserImage(6,sixthImage))
*/


    }

    fun loadImage(bitmap:Bitmap,imageView: ImageView) {
        val multi = MultiTransformation<Bitmap>(
            CropSquareTransformation(),
            RoundedCornersTransformation(16, 8, RoundedCornersTransformation.CornerType.ALL)
        )

        Glide.with(this).load(bitmap)
            .apply(RequestOptions.bitmapTransform(multi))
            .into(imageView)
    }
    fun loadImage(url:Any,imageView: ImageView){
        val multi = MultiTransformation<Bitmap>(
            CropSquareTransformation(),
            RoundedCornersTransformation(16, 8, RoundedCornersTransformation.CornerType.ALL))

        Glide.with(this).load(url)
            .apply(RequestOptions.bitmapTransform(multi))
            .into(imageView)

    }

    private fun updateAboutYou() {
        val txt = writeAboutYouEditText.text.toString()
        if (txt.isNotEmpty()) {
            SharedPrefrenceManager.setAboutYou(this, txt)
        }
    }

    private fun updateJobTitle() {
        val txt = jobTitleEditText.text.toString()
        if (txt.isNotEmpty()) {
            SharedPrefrenceManager.setJobTitle(this, txt)
        }
    }

    private fun updateCompanyName() {
        val txt = companyNameEditText.text.toString()
        if (txt.isNotEmpty()) {
            SharedPrefrenceManager.setCompanyName(this, txt)
        }
    }

    private fun updateHomeTown() {
        val txt = homeTownEditText.text.toString()
        if (txt.isNotEmpty()) {
            SharedPrefrenceManager.setHomeTown(this, txt)
        }
    }

    private fun updateSchool() {
        val txt = schoolOrUniversityEditText.text.toString()
        if (txt.isNotEmpty()) {
            SharedPrefrenceManager.setSchoolUniversity(this, txt)
        }
    }

    private fun setupChangeGenderBtn() {

        maleGenderBtn.setOnClickListener {
            //            if (Helpers.isGenderChangedAllowed(this)) {
            showGenderOnlyChangedOnceDialog(Constants.MALE.toLowerCase())
//            }
        }

        femaleGenderBtn.setOnClickListener {
            //            if (Helpers.isGenderChangedAllowed(this)) {
            showGenderOnlyChangedOnceDialog(Constants.FEMALE.toLowerCase())

//            }
        }
        otherGenderBtn.setOnClickListener {
            //            if (Helpers.isGenderChangedAllowed(this)) {
            showGenderOnlyChangedOnceDialog(Constants.OTHERS.toLowerCase())

//            }
        }

    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        addBasicInfoItemViews()
        adapter.notifyDataSetChanged()
    }

    private fun showGenderOnlyChangedOnceDialog(gender: String) {
        val yesButton: Button
        val noButton: Button
        val ll = LayoutInflater.from(this).inflate(R.layout.change_user_gender_only_once, null)
        val dialog = Dialog(this)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        yesButton = dialog.find(R.id.change_gender_yes_btn)
        noButton = dialog.find(R.id.change_gender_no_btn)
        yesButton.setOnClickListener {
            dialog.dismiss()
            setupSelectedGender(gender)
//            SharedPrefrenceManager.setUserGenderChanged(this,false)
            SharedPrefrenceManager.setUserGender(this, gender)


        }
        noButton.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()

    }

    private fun setupSelectedGender(selectedgender: String) {
        when (selectedgender) {
            Constants.MALE.toLowerCase() -> {
                maleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_selected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.white)

                femaleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                otherGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            Constants.FEMALE.toLowerCase() -> {
                femaleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_selected_btn)
                femaleGenderBtn.textColor = resources.getColor(R.color.white)

                maleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                otherGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            else -> {
                otherGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_selected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.white)

                maleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                femaleGenderBtn.background =
                    resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)
            }
        }
    }

    private fun setupFields() {
        val aboutYou = SharedPrefrenceManager.getAboutYou(this)
        val jobTitle = SharedPrefrenceManager.getJobTitle(this)
        val companyName = SharedPrefrenceManager.getCompanyName(this)
        val homeTown = SharedPrefrenceManager.getHomeTown(this)
        val school = SharedPrefrenceManager.getSchoolUniversity(this)

        if (aboutYou != Constants.NULL)
            writeAboutYouEditText.setText(aboutYou)
        if (jobTitle != Constants.NULL)
            jobTitleEditText.setText(jobTitle)
        if (companyName != Constants.NULL)
            companyNameEditText.setText(companyName)
        if (homeTown != Constants.NULL)
            homeTownEditText.setText(homeTown)
        if (school != Constants.NULL)
            schoolOrUniversityEditText.setText(school)


    }

    private fun setupRecyclerView() {
        userBasicInfoRecyclerView.adapter = adapter
        userBasicInfoRecyclerView.layoutManager = LinearLayoutManager(this)

        addBasicInfoItemViews()
    }

    private fun addBasicInfoItemViews() {
//        val basicInfoViewModel = ViewModelProviders.of(this).get(BasicInfoViewModel::class.java)
        val basicInfoViewModel = BasicInfoViewModel.getInstace(this.application)
        val basicModel = basicInfoViewModel.getData()

        for (model in basicModel) {
            adapter.add(ProfileBasicInfoItemView(this, model, this))
        }
    }



    private fun goToSetupProfileUploadPhotoActivity( flag: String) {
        launchActivity<SetupProfileUploadPhotoStep2Activity>
        {
            putExtra(imgBlock, flag)
            finish()
        }
    }

    fun StringToBitmap(img: String): Bitmap? {
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)

        }
        return bitmap
    }

    fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    inner class UserImagesAdapter: BaseAdapter<ImageModel>(itemLayoutRes = R.layout.item_image){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ImageModel> {
            return UserImageViewHolder(getItemView(parent))
        }

        inner class UserImageViewHolder(view: View) : BaseViewHolder<ImageModel>(view) {
            override fun fillData() {

                Log.i("ImageUser","${item!!.image}")
                if (item!!.image.isNullOrEmpty()){
                    itemView.btnDelete.visibility=View.GONE
                    itemView.ivProfileItem.setImageResource(R.drawable.add_image)
                }else {
                    itemView.btnDelete.visibility=View.VISIBLE
                    if (item!!.image!!.contains("http")) {
                        item!!.image?.let { loadImage(it, itemView.ivProfileItem) }
                    } else {
                        item!!.image?.let { StringToBitmap(it)?.let { it1 -> loadImage(it1, itemView.ivProfileItem) } }
                    }

                }
                itemView.ivProfileItem.setOnClickListener {
                    goToSetupProfileUploadPhotoActivity(item!!.id.toString())
                }

                itemView.setOnClickListener { onClick(it) }
                 itemView.btnDelete.setOnClickListener {
                     AskBeforeMakeActionDialog().show(this@UserProfileEditActivity,okAction = {
                         when(item!!.id){
                             1->{SharedPrefrenceManager.setFirstImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                             2->{
                                 SharedPrefrenceManager.setSecImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                             3->{
                                 SharedPrefrenceManager.setThirdImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                             4->{
                                 SharedPrefrenceManager.setFourthImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                             5->{
                                 SharedPrefrenceManager.setFiveImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                             6->{
                                 SharedPrefrenceManager.setSixImg(this@UserProfileEditActivity,"")
                                 itemView.btnDelete.visibility=View.GONE
                                 itemView.ivProfileItem.setImageDrawable(this@UserProfileEditActivity.resources.getDrawable(R.drawable.add_image))
                             }
                         }

                     })
                 }
            }

        }
    }

}
