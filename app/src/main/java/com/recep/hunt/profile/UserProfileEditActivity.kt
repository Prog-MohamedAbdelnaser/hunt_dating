package com.recep.hunt.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.profile.listeners.ProfileBasicInfoTappedListner
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.setupProfile.SetupProfileUploadPhotoStep2Activity
import com.recep.hunt.utilis.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_setup_profile_added_photo.*
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import kotlinx.android.synthetic.main.six_photos_item_layout.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import java.io.ByteArrayOutputStream
import java.io.File


class UserProfileEditActivity : BaseActivity(), ProfileBasicInfoTappedListner {

    override fun onItemClicked(position: Int, questionModel: UserBasicInfoQuestionModel, icon: Int) {
        launchActivity<UserProfileEditQuestionActivity> {
            putExtra(questionModelKey, questionModel)
            putExtra(questionPositionKey, position)
            putExtra(questionImageKey, icon)
        }
    }

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

    companion object {
        const val questionModelKey = "questionModelKey"
        const val questionImageKey = "questionImageKey"
        const val imgBlock = "imgBlock"
        const val questionPositionKey = "questionPositionKey"
    }


    private lateinit var userBasicInfoRecyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)
        setScreenTitle(R.string.edit_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
//        SharedPrefrenceManager.setUserGenderChanged(this, true)
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

        about_header.text = resources.getString(R.string.about_you, SharedPrefrenceManager.getUserFirstName(this))
        val dialog = Helpers.showDialog(this, this, "Updating Profile")
        oldGender = SharedPrefrenceManager.getUserGender(this);

        save_edit_profile_btn.setOnClickListener {
            dialog.show()
            updateAboutYou()
            updateJobTitle()
            updateCompanyName()
            updateHomeTown()
            updateSchool()
            updateGenderPrefrence()

            Run.after(2000) {
                dialog.dismiss()
                finish()

            }

        }

        bindSixImages()
        setupFields()
        setupRecyclerView()
        sharePrefeImageSetup()
        setupChangeGenderBtn()
        setupSelectedGender(SharedPrefrenceManager.getUserGender(this))

        ivPencilEditProfileId.setOnClickListener()
        {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false).start()
        }

    }

    private fun updateGenderPrefrence() {
        var gender = SharedPrefrenceManager.getUserGender(this)
        if(!gender.equals(oldGender)){
            SharedPrefrenceManager.setUserGenderChanged(this , false)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageFile: File
        if (requestCode === Config.RC_PICK_IMAGES && resultCode === Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            if (images.size == 1) {
                imageFile = File(images[0].path)
                MediaScannerConnection.scanFile(
                    this, arrayOf(imageFile.getAbsolutePath()), null
                ) { path, uri ->
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).start(this)
                }

            }
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                SharedPrefrenceManager.setProfileImg(this@UserProfileEditActivity, bitMapToString(bitmap))
                SharedPrefrenceManager.setsocialType(this@UserProfileEditActivity, "none")
                //ivEditProfielId.setImageURI(resultUri)
                ivEditProfielId.setImageBitmap(bitmap)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun sharePrefeImageSetup() {
        //ivEditProfielId.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getProfileImg(this)))
        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)
        val socialTypeStr = SharedPrefrenceManager.getsocialType(this)

        if (socialTypeStr.equals("social")) {
            Picasso.get().load(Uri.parse(SharedPrefrenceManager.getProfileImg(this)))
                .placeholder(R.drawable.account_icon).into(ivEditProfielId)
        } else {
            ivEditProfielId.setImageBitmap(StringToBitmap(SharedPrefrenceManager.getProfileImg(this)))
        }

        if (firstImage != Constants.NULL)
            user_image_1.setImageBitmap(StringToBitmap(firstImage))
        if (secondImage != Constants.NULL)
            user_image_2.setImageBitmap(StringToBitmap(secondImage))
        if (thirdImage != Constants.NULL)
            user_image_3.setImageBitmap(StringToBitmap(thirdImage))
        if (fourthImage != Constants.NULL)
            user_image_4.setImageBitmap(StringToBitmap(fourthImage))
        if (fifthImage != Constants.NULL)
            user_image_5.setImageBitmap(StringToBitmap(fifthImage))
        if (sixthImage != Constants.NULL)
            user_image_6.setImageBitmap(StringToBitmap(sixthImage))

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
            if (Helpers.isGenderChangedAllowed(this)) {
                showGenderOnlyChangedOnceDialog(Constants.MALE)

            }
        }

        femaleGenderBtn.setOnClickListener {
            if (Helpers.isGenderChangedAllowed(this)) {
                showGenderOnlyChangedOnceDialog(Constants.FEMALE)

            }
        }
        otherGenderBtn.setOnClickListener {
            if (Helpers.isGenderChangedAllowed(this)) {
                showGenderOnlyChangedOnceDialog(Constants.OTHERS)

            }
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
            Constants.MALE -> {
                maleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.white)

                femaleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                femaleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                otherGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            Constants.FEMALE -> {
                femaleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                femaleGenderBtn.textColor = resources.getColor(R.color.white)

                maleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                otherGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)
            }
            else -> {
                otherGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_selected_btn)
                otherGenderBtn.textColor = resources.getColor(R.color.white)

                maleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
                maleGenderBtn.textColor = resources.getColor(R.color.app_light_text_color)

                femaleGenderBtn.background = resources.getDrawable(R.drawable.profile_gender_unselected_btn)
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

    private fun bindSixImages() {
        user_image_1.setOnClickListener() {
            flag = 1
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
        user_image_2.setOnClickListener() {
            flag = 2
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
        user_image_3.setOnClickListener() {
            flag = 3
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
        user_image_4.setOnClickListener() {
            flag = 4
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
        user_image_5.setOnClickListener() {
            flag = 5
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
        user_image_6.setOnClickListener() {
            flag = 6
            launchActivity<SetupProfileUploadPhotoStep2Activity>
            {
                putExtra(imgBlock, flag.toString())
                finish()
            }
        }
    }

    fun StringToBitmap(img: String): Bitmap? {
        if (img != null) {
            var b = Base64.decode(img, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.size);

        }
        return bitmap
    }

    fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


}
