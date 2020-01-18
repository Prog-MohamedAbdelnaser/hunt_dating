package com.recep.hunt.application


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.login.WelcomeScreenActivity
import com.recep.hunt.matchs.di.matchQuestionsModule
import com.recep.hunt.utilis.AlertDialogUtils
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.OkListener
import com.recep.hunt.utilis.SharedPrefrenceManager
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.koin.android.ext.android.startKoin
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by RishabhShukla on 11/02/19.
 */


@ReportsCrashes(
    mailTo = "rudraksh.shukla98@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.add_job_title
)
class MyApplication : MultiDexApplication() {

    companion object {
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }



    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
                Log.i(TAG, "PACKAGE_NAME: $packageName")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(matchQuestionsModule))
        /** logger initialization **/
        Logger.addLogAdapter(AndroidLogAdapter())

        /** initialize sharedPreferenceManager **/
        SharedPrefrenceManager.init(this)
        /** setup basic shared pref **/
        Helpers.setupBasicSharedPrefrences(this)
//        ACRA.init(this);

        registerReceiver(mLogoutBroadcastReceiver, IntentFilter("ACTION_SESSION_EXPIRE"))


    }

    private val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

//    fun <T> addToRequestQueue(request: Request<T>) {
//        request.tag = TAG
//        requestQueue?.add(request)
//    }
//
//
//    fun cancelPendingRequests(tag: Any) {
//        if (requestQueue != null) {
//            requestQueue!!.cancelAll(tag)
//        }
//    }


    private val mLogoutBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_SESSION_EXPIRE") {
                unregisterReceiver(this)
                val message = intent.getStringExtra("INTENT_MESSAGE")
                Toast.makeText(context,message,Toast.LENGTH_LONG).show()

                context?.let { SharedPrefrenceManager.clearAllSharePreference(it) }

                val mIntent = Intent(
                    context,
                    WelcomeScreenActivity::class.java
                )
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(mIntent)


            }
        }
    }


}