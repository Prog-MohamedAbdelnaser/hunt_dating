package com.recep.hunt.application


import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by RishabhShukla on 11/02/19.
 */


@ReportsCrashes(
    mailTo = "rudraksh.shukla98@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText =R.string.add_job_title)
class MyApplication : MultiDexApplication() {

    companion object {
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized var instance: MyApplication? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        /** logger initialization **/
        Logger.addLogAdapter(AndroidLogAdapter())

        /** setup basic shared pref **/
        Helpers.setupBasicSharedPrefrences(this)
//        ACRA.init(this);
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

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }


    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }





    override fun onTerminate() {
        super.onTerminate()

    }



}