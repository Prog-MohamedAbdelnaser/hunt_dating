package com.recep.hunt.utilis

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.recep.hunt.R
import com.recep.hunt.login.WelcomeScreenActivity

object Utils {


    fun isSessionExpire(activity: Activity?, status: String, message: String) : Boolean{
        activity?.let {
            if(status == "9"){
                val builder = AlertDialog.Builder(it)
                builder.setTitle(it.getString(R.string.app_name))
                builder.setMessage(message)
                builder.setCancelable(false)

                builder.setPositiveButton("Ok"
                ) { dialog, which ->
                    activity.startActivity(Intent(activity, WelcomeScreenActivity::class.java))
                    activity.finishAffinity()
                }
                return true
            }
        }
        return false
    }

}