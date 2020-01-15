package com.recep.hunt.setupProfile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.utilis.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setup_profile_added_photo.*
import kotlinx.android.synthetic.main.activity_setup_profile_upload_photo.setup_profile_upload_pic_next_btn
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.File


class SetupProfileAddedPhotoActivity : BaseActivity() {

    lateinit var uri: Uri
    var bitmap: Bitmap? = null
    var avatarFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_added_photo)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }

    private fun init() {
        avatarFilePath = intent.getStringExtra(IMGURI)
        LogUtil.e("SetupProfileAddedPhotoActivity","Uri "+avatarFilePath)
        var profileImg = SharedPrefrenceManager.getProfileImg(this@SetupProfileAddedPhotoActivity)
        SharedPrefrenceManager.setUserImage(this@SetupProfileAddedPhotoActivity, profileImg)


        Glide.with(this)
            .load(avatarFilePath)
            .into(profile_image)

        change_pic_tv.setOnClickListener {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false).start()
        }
        setup_profile_upload_pic_next_btn.setOnClickListener {
            launchActivity<SetupProfileGenderActivity>{ putExtra(IMGURI, avatarFilePath)}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
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
                avatarFilePath = result.uri.toString()
                val resultUri = result.uri
                var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                SharedPrefrenceManager.setProfileImg(this@SetupProfileAddedPhotoActivity, bitMapToString(bitmap))
                profile_image.setImageURI(resultUri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

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
