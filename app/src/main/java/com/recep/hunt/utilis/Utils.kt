package com.recep.hunt.utilis

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.recep.hunt.login.WelcomeScreenActivity
import org.json.JSONObject


interface OkListener {
    fun ok()
}


object Utils {

    private var isDisplay = true

    fun isSessionExpire(context: Context?, errorJsonString: String?): Boolean {
        errorJsonString?.let { it1 ->
            if (it1.isNotEmpty()) {
                context?.let {
                    val mJsonObject = JSONObject(errorJsonString)
                    val status = mJsonObject.optString("status")
                    val message = mJsonObject.optString("message")
                    if (status == "9") {
                        if (isDisplay) {
                            isDisplay = false
                            AlertDialogUtils.displayDialog(context, message, object : OkListener {
                                override fun ok() {
                                    isDisplay = true
                                    SharedPrefrenceManager.clearAllSharePreference(context)
                                    context.startActivity(
                                        Intent(
                                            context,
                                            WelcomeScreenActivity::class.java
                                        )
                                    )
                                    (context as Activity).finishAffinity()
                                }
                            })
                        }
                        return true
                    }
                }
            }
        }

        return false
    }

}