package com.recep.hunt.utilis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.recep.hunt.login.WelcomeScreenActivity
import org.json.JSONObject


interface OkListener {
    fun ok()
}


object Utils {

    fun isSessionExpire(context: Context?, errorJsonString: String?): Boolean {
        errorJsonString?.let { it1 ->
            if (it1.isNotEmpty()) {
                context?.let {
                    val mJsonObject = JSONObject(errorJsonString)
                    val status = mJsonObject.optString("status")
                    val message = mJsonObject.optString("message")
                    if (status == "9") {
                        val mIntent = Intent("ACTION_SESSION_EXPIRE")
                        mIntent.putExtra("INTENT_MESSAGE",message)
                        context.sendBroadcast(mIntent)
                        return true
                    }
                }
            }
        }

        return false
    }

}