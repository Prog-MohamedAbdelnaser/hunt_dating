package com.recep.hunt.setupProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.recep.hunt.R
import com.recep.hunt.constants.APIUtils
import com.recep.hunt.home.HomeActivity
import com.recep.hunt.login.SocialLoginActivity
import com.recep.hunt.profile.viewmodel.UserViewModel
import com.recep.hunt.utilis.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_setup_profile_completed.*
import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.CoreProtocolPNames
import org.apache.http.util.EntityUtils
import org.jetbrains.anko.find
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.IOException
import java.net.URL

class SetupProfileCompletedActivity : AppCompatActivity() {

    private var mHttpClient: DefaultHttpClient? = null
    private lateinit var userImage: CircleImageView
    private lateinit var userName: TextView
    private lateinit var userViewModel: UserViewModel
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setup_profile_completed)
        init()
    }

    private fun init() {
        userImage = find(R.id.completed_profile_user_image)
        userName = find(R.id.user_completed_profile_name)
        setSupportActionBar(setupProfile_complete_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        setupViews()

        SharedPrefrenceManager.setUserGenderChanged(this, true)
        lottieAnimationView.playAnimation()

        lets_start_btn.setOnClickListener {
            SharedPrefrenceManager.setIsLoggedIn(this, true)
            SharedPrefrenceManager.setIsOtpVerified(this,true)
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setupViews() {
        val userImageString = SharedPrefrenceManager.getProfileImg(this)
        userImage.setImageBitmap(StringToBitmap(userImageString))
        //val userImageString = SharedPrefrenceManager.getUserImage(this)
        //Picasso.get().load(Uri.parse(userImageString)).placeholder(R.drawable.account_icon).into(userImage)
        val firstName = SharedPrefrenceManager.getUserFirstName(this)
        val lastName = SharedPrefrenceManager.getUserLastName(this)

        userName.text = "$firstName $lastName"
    }

    private fun insertUserIntoDb() {
        val firstName = SharedPrefrenceManager.getUserFirstName(this)
        val lastName = SharedPrefrenceManager.getUserLastName(this)
        val gender = SharedPrefrenceManager.getUserGender(this)
        val email = SharedPrefrenceManager.getUserEmail(this)
        val userDob = SharedPrefrenceManager.getUserDob(this)
        val userImage = SharedPrefrenceManager.getUserImage(this)
//        val user = User(firstName,lastName,email,gender,userDob,userImage,)
//        userViewModel.insert(user)
    }

    inner class MakeAPICall(val activity: Activity, val context: Context) : AsyncTask<String, Void, String>() {
        var receivedResponse = ""
        val dialog = Helpers.showDialog(activity, context, "Setting up your profile").show()
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): String {
//            val dialog =
            val url = URL(params[0])
            val request = Multipart(url)
            val first_name = SharedPrefrenceManager.getUserFirstName(context)
            val last_name = SharedPrefrenceManager.getUserLastName(context)
//        val mobile_no = SharedPrefrenceManager.getUserMobileNumber(this)
            val mobile_no = "9650900979"
//        val country_code = SharedPrefrenceManager.getUserCountryCode(this)
            val country_code = "+91"
            val gender = SharedPrefrenceManager.getUserGender(context)
            val dob = SharedPrefrenceManager.getUserDob(context)
//            val email = SharedPrefrenceManager.getUserEmail(context)
            val email = "rish@1450@gmail.com"
//        val country = SharedPrefrenceManager.getUserFirstName(this)
            val country = "India"
            val lat = SharedPrefrenceManager.getUserLatitude(context)
            val lang = SharedPrefrenceManager.getUserLongitude(context)
            val device_type = "android"
            val device_id = "1"
            val device_token = SharedPrefrenceManager.getDeviceToken(context)
            val for_date = SharedPrefrenceManager.getUserInterestedIn(context)
            val for_bussiness = SharedPrefrenceManager.getUserInterestedIn(context)
            val for_friendship = SharedPrefrenceManager.getUserInterestedIn(context)
            val fb_id = ""
            val fb_token = ""
            val insta_id = ""
            val insta_token = ""
            val google_id = ""
            val google_token = ""
            val reference_code = ""

            request.addFormField("first_name", first_name)
            request.addFormField("last_name", last_name)
            request.addFormField("mobile_no", mobile_no)
            request.addFormField("country_code", country_code)
            request.addFormField("gender", gender)
            request.addFormField("dob", dob)
            request.addFormField("email", email)
            request.addFormField("country", country)
            request.addFormField("lat", lat)
            request.addFormField("lang", lang)
            request.addFormField("device_type", device_type)
            request.addFormField("device_id", device_id)
            request.addFormField("device_token", device_token)
            request.addFormField("for_date", for_date)
            request.addFormField("for_bussiness", for_bussiness)
            request.addFormField("for_friendship", for_friendship)
            request.addFormField("fb_id", fb_id)
            request.addFormField("fb_token", fb_token)
            request.addFormField("insta_id", insta_id)
            request.addFormField("insta_token", insta_token)
            request.addFormField("google_id", google_id)
            request.addFormField("google_token", google_token)
            request.addFormField("reference_code", reference_code)
            val selectedFileUri = SharedPrefrenceManager.getUserImage(context)
            val selectedFilePath = FilePath.getPath(applicationContext, Uri.parse(selectedFileUri))
            request.addFilePart("profile_pic", File(selectedFilePath), selectedFileUri, ".jpg")


            val listener = object : Multipart.OnFileUploadedListener {

                override fun onFileUploadingSuccess(response: String) {
                    dialog.dismiss()
                    Log.e(SetupProfileCompletedActivity::class.java.simpleName, "Json Response - $response")
                    lottieAnimationView.playAnimation()
                    receivedResponse = response
                }

                override fun onFileUploadingFailed(responseCode: Int) {
                    Log.e(SetupProfileCompletedActivity::class.java.simpleName, "Json Failed - $responseCode")

                }

            }

            request.upload(listener)

            return receivedResponse
        }

        override fun onPostExecute(result: String?) {
            dialog.dismiss()
        }
    }

    fun register() {
        val dialog = Helpers.showDialog(this, this, "Setting up your profile").show()
        val url = URL(APIUtils.REGISTER)
        val request = Multipart(url)
        val first_name = SharedPrefrenceManager.getUserFirstName(this)
        val last_name = SharedPrefrenceManager.getUserLastName(this)
//        val mobile_no = SharedPrefrenceManager.getUserMobileNumber(this)
        val mobile_no = "9650900979"
//        val country_code = SharedPrefrenceManager.getUserCountryCode(this)
        val country_code = "+91"
        val gender = SharedPrefrenceManager.getUserGender(this)
        val dob = SharedPrefrenceManager.getUserDob(this)
        val email = SharedPrefrenceManager.getUserEmail(this)
//        val country = SharedPrefrenceManager.getUserFirstName(this)
        val country = "India"
        val lat = SharedPrefrenceManager.getUserLatitude(this)
        val lang = SharedPrefrenceManager.getUserLongitude(this)
        val device_type = "android"
        val device_id = "1"
        val device_token = SharedPrefrenceManager.getDeviceToken(this)
        val for_date = SharedPrefrenceManager.getUserInterestedIn(this)
        val for_bussiness = SharedPrefrenceManager.getUserInterestedIn(this)
        val for_friendship = SharedPrefrenceManager.getUserInterestedIn(this)
        val fb_id = ""
        val fb_token = ""
        val insta_id = ""
        val insta_token = ""
        val google_id = ""
        val google_token = ""
        val reference_code = ""

        request.addFormField("first_name", first_name)
        request.addFormField("last_name", last_name)
        request.addFormField("mobile_no", mobile_no)
        request.addFormField("country_code", country_code)
        request.addFormField("gender", gender)
        request.addFormField("dob", dob)
        request.addFormField("email", email)
        request.addFormField("country", country)
        request.addFormField("lat", lat)
        request.addFormField("lang", lang)
        request.addFormField("device_type", device_type)
        request.addFormField("device_id", device_id)
        request.addFormField("device_token", device_token)
        request.addFormField("for_date", for_date)
        request.addFormField("for_bussiness", for_bussiness)
        request.addFormField("for_friendship", for_friendship)
        request.addFormField("fb_id", fb_id)
        request.addFormField("fb_token", fb_token)
        request.addFormField("insta_id", insta_id)
        request.addFormField("insta_token", insta_token)
        request.addFormField("google_id", google_id)
        request.addFormField("google_token", google_token)
        request.addFormField("reference_code", reference_code)
        val selectedFileUri = SharedPrefrenceManager.getUserImage(this)
        val selectedFilePath = FilePath.getPath(applicationContext, Uri.parse(selectedFileUri))
        request.addFilePart("profile_pic", File(selectedFilePath), selectedFileUri, ".jpg")


        val listener = object : Multipart.OnFileUploadedListener {

            override fun onFileUploadingSuccess(response: String) {
                dialog.dismiss()
                Log.e(SetupProfileCompletedActivity::class.java.simpleName, "Json Response - $response")
                lottieAnimationView.playAnimation()
            }

            override fun onFileUploadingFailed(responseCode: Int) {


            }

        }

        request.upload(listener)
    }

    private fun registerUser(completion: () -> Unit) {
        val params = BasicHttpParams()
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1)
        mHttpClient = DefaultHttpClient(params)
        try {

            val httppost = HttpPost(APIUtils.REGISTER)
            Log.e(SetupProfileCompletedActivity::class.java.simpleName, "params :${APIUtils.REGISTER}")
            val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
            val interestArea = SharedPrefrenceManager.getUserInterestedIn(this)
//            multipartEntity.addPart("first_name",StringBody())
            multipartEntity.addPart("last_name", StringBody(SharedPrefrenceManager.getUserLastName(this)))
//            multipartEntity.addPart("mobile_no",StringBody(SharedPrefrenceManager.getUserMobileNumber(this)))
            multipartEntity.addPart("mobile_no", StringBody("9650900979"))
//            multipartEntity.addPart("country_code",StringBody(SharedPrefrenceManager.getUserCountryCode(this)))
            multipartEntity.addPart("country_code", StringBody("+91"))
            multipartEntity.addPart("gender", StringBody(SharedPrefrenceManager.getUserGender(this)))
            multipartEntity.addPart("dob", StringBody(SharedPrefrenceManager.getUserDob(this)))
            multipartEntity.addPart("email", StringBody(SharedPrefrenceManager.getUserEmail(this)))
//            multipartEntity.addPart("email",StringBody("ris"))
//            multipartEntity.addPart("country",StringBody(SharedPrefrenceManager.getUserCountry(this)))
            multipartEntity.addPart("country", StringBody("India"))
            multipartEntity.addPart("lat", StringBody(SharedPrefrenceManager.getUserLatitude(this)))
            multipartEntity.addPart("lang", StringBody(SharedPrefrenceManager.getUserLongitude(this)))
            multipartEntity.addPart("device_type", StringBody("android"))
            multipartEntity.addPart("device_id", StringBody("1"))
            multipartEntity.addPart("device_token", StringBody(SharedPrefrenceManager.getDeviceToken(this)))
            multipartEntity.addPart("for_date", StringBody(interestArea))
            multipartEntity.addPart("for_bussiness", StringBody(interestArea))
            multipartEntity.addPart("for_friendship", StringBody(interestArea))
            multipartEntity.addPart("fb_id", StringBody(""))
            multipartEntity.addPart("fb_token", StringBody(""))
            multipartEntity.addPart("insta_id", StringBody(""))
            multipartEntity.addPart("insta_token", StringBody(""))
            multipartEntity.addPart("google_id", StringBody(""))
            multipartEntity.addPart("google_token", StringBody(""))
            multipartEntity.addPart("reference_code", StringBody(""))
            val selectedFileUri = SharedPrefrenceManager.getUserImage(this)
            val selectedFilePath = FilePath.getPath(applicationContext, Uri.parse(selectedFileUri))
            multipartEntity.addPart("profile_pic", FileBody(File(selectedFilePath)))

            Log.e(SetupProfileCompletedActivity::class.java.simpleName, "params :$multipartEntity")
            httppost.entity = multipartEntity
            Thread {
                //Do some Network Request
                mHttpClient!!.execute(httppost, PhotoUploadResponseHandler())
                runOnUiThread {
                    //Update UI
                    toast("Done")
                    completion()
//                    Helpers.showSimpleSnackbar(this@PTIRequestActivity,"SUCCESS","Assignment was uploaded successfully",R.color.cyan_green,R.color.white,R.color.white, CookieBar.TOP)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private inner class PhotoUploadResponseHandler : ResponseHandler<Any> {
        @Throws(ClientProtocolException::class, IOException::class)
        override fun handleResponse(response: HttpResponse): Any? {
            val httpEntity = response.entity
            val responseString = EntityUtils.toString(httpEntity)
            try {
                val jsonParser = JSONParser()
                val jsonObject = jsonParser.parse(responseString) as org.json.simple.JSONObject
                applicationContext.runOnUiThread {
                    Log.e(SetupProfileCompletedActivity::class.java.simpleName, "Json Response - $jsonObject")
                    if (jsonObject["message"].toString() == "Register Successfull") {
                        toast("Success")
                    } else {
                        toast("FAILED")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
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
