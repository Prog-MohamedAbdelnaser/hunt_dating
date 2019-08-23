package com.recep.hunt.setupProfile

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_setup_profile_upload_photo_step2.*
import org.jetbrains.anko.toast
import java.io.File
import android.R.attr.data
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.utilis.launchActivity
import com.theartofdev.edmodo.cropper.CropImageView


class SetupProfileUploadPhotoStep2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.recep.hunt.R.layout.activity_setup_profile_upload_photo_step2)
        init()
    }

    private fun init() {
        setSupportActionBar(setupProfileupload_pic1__toolbar)

        camera_layout.setOnClickListener {
            ImagePicker.with(this).setCameraOnly(true).setMultipleMode(false)
                .start()

        }

        gallery_layout.setOnClickListener {
            ImagePicker.with(this).setShowCamera(false).setMultipleMode(false)
                .start()

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

            toast("selected")
            // do your logic here...
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                launchActivity<SetupProfileAddedPhotoActivity> { putExtra(IMGURI,resultUri.toString())  }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}
