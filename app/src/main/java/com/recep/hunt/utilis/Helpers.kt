package com.recep.hunt.utilis

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.recep.hunt.constants.Constants


/**
 * Created by Rishabh Shukla
 * on 2019-08-18
 * Email : rishabh1450@gmail.com
 */

class Helpers {

    companion object{
        fun setSharedPrefs(context: Context, key:String, value:String){
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun getSharePrefs(context: Context, key:String) : String {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            return sharedPreferences.getString(key,"null") ?: ""
        }

        fun isInternetConnection(context: Context): Boolean {
            val cn =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nf = cn.activeNetworkInfo
            val statusInternet: Boolean

            statusInternet = if (nf != null && nf.isConnected) {
                Log.i("Info:", "Network Available.")
                true
            } else {
                Log.i("Info:", "Network Not Available.")
                false

            }
            return statusInternet
        }
    }

}