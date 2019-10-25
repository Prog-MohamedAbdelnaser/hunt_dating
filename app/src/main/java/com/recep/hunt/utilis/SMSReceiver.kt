package com.recep.hunt.utilis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSReceiver : BroadcastReceiver(){

    private var listener: Listener? = null

    fun injectListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {

                CommonStatusCodes.SUCCESS -> {

                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String

                    val pattern = Pattern.compile("\\d{6}")
                    val matcher = pattern.matcher(message)

                    if (matcher.find()){
                        Toast.makeText(context, "Your OTP is :" + matcher.group(0), Toast.LENGTH_SHORT).show()
                        listener?.onSMSReceived(matcher.group(0))
                    }

                    else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }

                }
                CommonStatusCodes.TIMEOUT -> listener?.onTimeOut()
            }
        }
    }

    interface Listener {
        fun onSMSReceived(otp: String)
        fun onTimeOut()
    }

}