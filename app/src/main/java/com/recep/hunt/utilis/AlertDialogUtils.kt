package com.recep.hunt.utilis

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.recep.hunt.R



object AlertDialogUtils {

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
