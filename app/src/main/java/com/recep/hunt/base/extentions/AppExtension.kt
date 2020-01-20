package com.recep.hunt.base.extentions

import android.app.Activity
import android.util.Log
import com.recep.hunt.FeaturesConstants.EXPIRE_SESSION_LIST_STATUS
import com.recep.hunt.R
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.data.exceptions.APIException
import com.recep.hunt.utilis.Utils
import org.koin.android.ext.android.inject
import retrofit2.HttpException


inline fun Throwable.handleApiError(action: (String, String) -> Unit) {

    if (this is HttpException) {
        if (this.message!=null) {

            action(code().toString(), this.message())
        }else{
            val message ="unsepcified error !!"
            action(code().toString(), message)
        }

    } else if (this is APIException){
        val errorMessage: String = message!!
        action(this.code,errorMessage)
        Log.d("API EXCEPTION", errorMessage)
    } else {
        val errorMessage: String = message!!
        action("Undefine",errorMessage)
        Log.d("Not_HTTP", errorMessage)
    }

}


fun Activity.handleApiErrorWithSnackBar(e: Throwable) {
    e.handleApiError { status, message ->
        Log.i("handleApiErrorWith","handleApiError:$status ${status in EXPIRE_SESSION_LIST_STATUS}")

        if (status in EXPIRE_SESSION_LIST_STATUS) {
          Utils.sendExpireSessionToBroascast(message,this)
        }else{
            Helpers.showErrorSnackBar(this,getString(R.string.notice),message)
        }
    }

}



