package com.recep.hunt.utilis

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.recep.hunt.R



object AlertDialogUtils {

//    private var alertDialog: AlertDialog? = null
//
//    fun showErrorDialog(context: Context, message: String,okListener: OkListener? = null) {
//        if (alertDialog != null && alertDialog!!.isShowing)
//            return
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle(context.resources.getString(R.string.app_name))
//        builder.setMessage(message)
//        builder.setCancelable(false)
//        builder.setPositiveButton("OK") { dialogInterface, _ ->
////            dialogInterface.dismiss()
//            dismiss()
//            okListener?.ok()
//
//
//        }
//
//        alertDialog = builder.create()
//        alertDialog!!.show()
////        alertDialog!!.setCanceledOnTouchOutside(false)
//    }
//
//    val isShowing: Boolean
//        get() = alertDialog != null && alertDialog!!.isShowing
//
//    fun dismiss() {
//        if (isShowing) {
//            alertDialog!!.dismiss()
//            alertDialog = null
//        }
//    }
//


    fun displayDialog(context: Context, message: String,okListener: OkListener? = null){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialogInterface, _ ->
            okListener?.ok()
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
