package com.recep.hunt.setupProfile

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.recep.hunt.R
import com.recep.hunt.constants.APIUtils
import com.recep.hunt.constants.Constants
import com.recep.hunt.location.TurnOnGPSActivity
import com.recep.hunt.utilis.FilePath
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
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
import org.aviran.cookiebar2.CookieBar
import org.jetbrains.anko.error
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.IOException
import java.util.ArrayList

class SetupProfileCompletedActivity : AppCompatActivity() {

    private var mHttpClient: DefaultHttpClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_completed)
        init()
    }

    private fun init() {
        setSupportActionBar(setupProfile_complete_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        lets_start_btn.setOnClickListener {
            launchActivity<TurnOnGPSActivity>()
        }
    }

    private fun registerUser() {
        val params = BasicHttpParams()
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1)
        mHttpClient = DefaultHttpClient(params)
        try {

            val httppost = HttpPost(APIUtils.REGISTER)
            val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)

            multipartEntity.addPart("first_name",StringBody(SharedPrefrenceManager.getUserFirstName(this)))
            multipartEntity.addPart("last_name",StringBody(SharedPrefrenceManager.getUserLastName(this)))
            multipartEntity.addPart("mobile_no",StringBody(""))
            multipartEntity.addPart("country_code",StringBody(""))
            multipartEntity.addPart("gender",StringBody(""))
            multipartEntity.addPart("dob",StringBody(""))
//            multipartEntity.addPart("profile_pic",StringBody(""))
            multipartEntity.addPart("email",StringBody(""))
            multipartEntity.addPart("country",StringBody(""))
            multipartEntity.addPart("lat",StringBody(""))
            multipartEntity.addPart("lang",StringBody(""))
            multipartEntity.addPart("device_type",StringBody("android"))
            multipartEntity.addPart("device_id",StringBody(""))
            multipartEntity.addPart("device_token",StringBody(""))
            multipartEntity.addPart("for_date",StringBody(""))
            multipartEntity.addPart("for_bussiness",StringBody(""))
            multipartEntity.addPart("for_friendship",StringBody(""))
            multipartEntity.addPart("fb_id",StringBody(""))
            multipartEntity.addPart("fb_token",StringBody(""))
            multipartEntity.addPart("insta_id",StringBody(""))
            multipartEntity.addPart("insta_token",StringBody(""))
            multipartEntity.addPart("google_id",StringBody(""))
            multipartEntity.addPart("google_token",StringBody(""))
            multipartEntity.addPart("reference_code",StringBody(""))
            val selectedFileUri = SharedPrefrenceManager.getUserImage(this)
            val selectedFilePath = FilePath.getPath(applicationContext, Uri.parse(selectedFileUri))
            multipartEntity.addPart("profile_pic]", FileBody(File(selectedFilePath)))

            httppost.entity = multipartEntity
            Thread {
                //Do some Network Request
                mHttpClient!!.execute(httppost,PhotoUploadResponseHandler())
                runOnUiThread {
                    //Update UI
                    finish()
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
            val httpEntity= response.entity
            val responseString = EntityUtils.toString(httpEntity)
            try {
                val jsonParser = JSONParser()
                val jsonObject = jsonParser.parse(responseString) as org.json.simple.JSONObject
                applicationContext.runOnUiThread {
                    if(jsonObject["message"].toString() == "Register Successfull"){
//                        Helpers.showSimpleSnackbar(this@PTIRequestActivity,"SUCCESS","Assignment was uploaded successfully",R.color.cyan_green,R.color.white,R.color.white,CookieBar.TOP)
//                        finish()
                        toast("Success")
                    }else{
                        toast("FAILED")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
