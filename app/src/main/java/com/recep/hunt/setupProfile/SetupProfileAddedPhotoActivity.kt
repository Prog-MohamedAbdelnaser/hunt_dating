package com.recep.hunt.setupProfile

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.recep.hunt.R
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setup_profile_added_photo.*
import kotlinx.android.synthetic.main.activity_setup_profile_upload_photo.setup_profile_upload_pic_next_btn
import org.jetbrains.anko.toast
import java.io.File


class SetupProfileAddedPhotoActivity : AppCompatActivity() {

    lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_added_photo)
        init()
    }

    private fun init() {
        setSupportActionBar(setupProfileupload_pic3__toolbar)
        val uriString: String = intent.getStringExtra(IMGURI)
        SharedPrefrenceManager.setUserImage(this@SetupProfileAddedPhotoActivity,uriString)
        uri = Uri.parse(uriString)
        profile_image.setImageURI(uri)


        change_pic_tv.setOnClickListener {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false)
                .start()
        }
        setup_profile_upload_pic_next_btn.setOnClickListener {
            launchActivity<SetupProfileGenderActivity>()
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
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL)
                        .start(this)

                }

            }
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
             profile_image.setImageURI(resultUri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

}
