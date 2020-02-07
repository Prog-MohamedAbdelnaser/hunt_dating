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
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nguyenhoanglam.imagepicker.helper.ImageHelper.createImageFile
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.constants.Constants.Companion.IMGURI
import com.recep.hunt.profile.UserProfileEditActivity
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.LogUtil
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.theartofdev.edmodo.cropper.CropImageView
import org.jetbrains.anko.Android
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.security.cert.Extension
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class SetupProfileUploadPhotoStep2Activity : BaseActivity() {
    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private val WRITE_EXTERNAL_STORAGE_CODE =13
    }

    private var imgFlag: String? = null
    private var mPath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_upload_photo_step2)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        SharedPrefrenceManager.setUserGenderChanged(this, true)
        init()
    }

    private fun init() {
        imgFlag = intent.getStringExtra(UserProfileEditActivity.imgBlock)
        Logger.d("imgFlag from intent ",imgFlag)
        camera_layout.setOnClickListener { takePhoto() }
        gallery_layout.setOnClickListener {
            setupPermissions()
        }

    }

    private fun setupPermissions() {
        val permissionWrite = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val permissionRead = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionWrite != PackageManager.PERMISSION_GRANTED ||
                permissionRead != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        else{
            selectImageInAlbum()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE_CODE)
    }

    private fun selectImageInAlbum() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
//        }

        ImagePicker.with(this)
            .setShowCamera(false)
            .setMultipleMode(false)
            .start()
    }

    private var currentPhotoPath = ""
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private var count = 1

    private fun takePhoto() {
        val dir =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}+/huntCameraPictures/"
        val newDir = File(dir)
        newDir.mkdir()
        if (checkPermission()) {
            count++
            val file = "$newDir$count.jpg"
            try {
                val newFile = File(file)
//                val opURI = Uri.fromFile(newFile)
                val opURI = FileProvider.getUriForFile(this, "$packageName.fileprovider", newFile)
                val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent1.putExtra(MediaStore.EXTRA_OUTPUT,opURI)
                startActivityForResult(intent1, 9)

            } catch (e: Exception) {
                Log.e("Exception", ":$e")
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermission()

    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
        }
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK && data != null) {
            val images = data.data
            val imagesBtm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), images)
            if (imgFlag == null) {
                if (images != null) {
                    SharedPrefrenceManager.setProfileImg(this, BitMapToString(imagesBtm))
                    launchActivity<SetupProfileAddedPhotoActivity> { putExtra(IMGURI, images.toString()) }
                }
            } else {
                setImage(imagesBtm)
            }
        } else if (requestCode == 9 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e("QuestionData Extras : ", " ${data.extras}")
            val images = data.extras.get("questionData") as Bitmap
            val imageString = BitMapToString(images)
            if (imgFlag == null) {
                SharedPrefrenceManager.setProfileImg(this, imageString)
                launchActivity<SetupProfileAddedPhotoActivity> { putExtra(IMGURI, data.data.toString()) }
            } else {
                setImage(images)
            }
        }

        /** image picker and cropper **/
        val imageFile: File
        if (requestCode === Config.RC_PICK_IMAGES && resultCode === Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            if (images.size == 1) {
                imageFile = File(images[0].path)
                MediaScannerConnection.scanFile(
                    this, arrayOf(imageFile.getAbsolutePath()), null
                ) { path, uri ->
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).start(this)
                    Logger.d("path = $path")
                }

            }
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)


            if (resultCode === Activity.RESULT_OK) {
                LogUtil.d("Uri ",result.uri.toString())
                if (imgFlag.isNullOrEmpty()) {
                    LogUtil.d("imageFlag","Called")

//                    SharedPrefrenceManager.setProfileImg(this, result.bitmap.toString())
                    launchActivity<SetupProfileAddedPhotoActivity> {
                        putExtra(
                            IMGURI,
                            result.uri.toString()
                        )
                    }
                }
                else{
                    setImage(MediaStore
                        .Images
                        .Media
                        .getBitmap(this.getContentResolver(),
                            result.uri))
                }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun setImage(bitmap: Bitmap) {
        val imageString = BitMapToString(bitmap)
        if (imgFlag.equals("1")) {
            SharedPrefrenceManager.setFirstImg(this, imageString)
            finishActivity()
        } else if (imgFlag.equals("2")) {
            SharedPrefrenceManager.setSecImg(this, imageString)
            finishActivity()
        } else if (imgFlag.equals("3")) {
            SharedPrefrenceManager.setThirdImg(this, imageString)
            finishActivity()
        } else if (imgFlag.equals("4")) {
            SharedPrefrenceManager.setFourthImg(this, imageString)
            finishActivity()
        } else if (imgFlag.equals("5")) {
            SharedPrefrenceManager.setFiveImg(this, imageString)
            finishActivity()
        } else {
            SharedPrefrenceManager.setSixImg(this, imageString)
            finishActivity()
        }

    }

    private fun finishActivity() {
        launchActivity<UserProfileEditActivity>
        {
            finish()
        }
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "title", null)
        return Uri.parse(path)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent1.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent1, 9)
                    }
                }
            }

        }
    }

}
