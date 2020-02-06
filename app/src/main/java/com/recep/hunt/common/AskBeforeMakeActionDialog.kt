package com.recep.hunt.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.recep.hunt.R
import kotlinx.android.synthetic.main.report_user_question_dialog_layout.*

class AskBeforeMakeActionDialog {

     fun show(context: Context,okAction:() -> Unit) {
        val ll =
            LayoutInflater.from(context).inflate(R.layout.report_user_question_dialog_layout, null)
        val dialog = Dialog(context)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.btnNo.setOnClickListener { dialog.dismiss() }
        dialog.btnYes.setOnClickListener {
            okAction()
            dialog.dismiss()
        }

        dialog.show()

    }
}