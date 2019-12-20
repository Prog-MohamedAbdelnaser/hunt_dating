package com.recep.hunt.utilis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.inputmethod.InputMethodManager
import com.recep.hunt.login.WelcomeScreenActivity
import org.json.JSONException
import org.json.JSONObject
import androidx.core.content.ContextCompat.getSystemService




interface OkListener {
    fun ok()
}


object Utils {

    fun isSessionExpire(context: Context?, errorJsonString: String?): Boolean {
        errorJsonString?.let { it1 ->
            if (it1.isNotEmpty()) {
                context?.let {
                    try{
                        val mJsonObject = JSONObject(errorJsonString)
                        val status = mJsonObject.optString("status")
                        val message = mJsonObject.optString("message")
                        if (status == "9") {
                            val mIntent = Intent("ACTION_SESSION_EXPIRE")
                            mIntent.putExtra("INTENT_MESSAGE",message)
                            context.sendBroadcast(mIntent)
                            return true
                        }
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }

                }
            }
        }

        return false
    }

    fun hideKeyboard(mActivity: Activity?){
        val view = mActivity?.currentFocus
        if (view != null) {
            val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

}