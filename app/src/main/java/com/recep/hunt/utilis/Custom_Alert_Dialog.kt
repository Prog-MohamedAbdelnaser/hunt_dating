package com.recep.hunt.utilis

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.recep.hunt.R
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alert


/**
 * Created by Rishabh Shukla
 * on 2019-08-18
 * Email : rishabh1450@gmail.com
 */

class Custom_Alert_Dialog(ui: AnkoContext<View>,context: Context) {
    lateinit var dialog: DialogInterface
//    lateinit var okButton: TextView

    init {
        with(ui) {

            dialog = alert{
                val view = LayoutInflater.from(context).inflate(R.layout.resend_otp_dialog_layout, null)
                customView = view
            }.show()
        }
    }

}