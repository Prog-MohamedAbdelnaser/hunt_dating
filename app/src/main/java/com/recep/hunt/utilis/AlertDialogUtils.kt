package com.recep.hunt.utilis

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.recep.hunt.R


/**
 * Created by Solution Analysts
 *
 *
 */

object AlertDialogUtils {

    private var alertDialog: AlertDialog? = null

    fun showErrorDialog(context: Context, message: String) {
        if (alertDialog != null && alertDialog!!.isShowing)
            return
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialogInterface, _ -> dialogInterface.dismiss() }

        alertDialog = builder.create()
        alertDialog!!.show()
        alertDialog!!.setCanceledOnTouchOutside(false)
    }

    val isShowing: Boolean
        get() = alertDialog != null && alertDialog!!.isShowing

    fun dismiss() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
            alertDialog = null
        }
    }
}
