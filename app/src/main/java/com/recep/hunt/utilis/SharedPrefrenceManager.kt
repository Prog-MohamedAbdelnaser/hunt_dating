package com.recep.hunt.utilis

import android.content.Context
import com.recep.hunt.constants.Constants


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */
enum class PersistenceKeys {
    UserName,UserId,UserImage,UserEmail,IsOtpVerified
}
class SharedPrefrenceManager {
    companion object{
        private fun setSharedPrefs(context: Context, key:String, value:String){
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
        private fun getSharePrefs(context: Context, key:String) : String {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            return sharedPreferences.getString(key,"null") ?: ""
        }

        //ISOTpVerified
        fun setIsOtpVerified(context: Context,value: Boolean){
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsOtpVerified.toString(),value)
            editor.apply()
        }
        fun getIsOtpVerified(context: Context):Boolean{
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName,0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsOtpVerified.toString(),false)
        }

        //USERID
        fun setUserId(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.UserId.toString(),value)
        }
        fun getUserId(context: Context):String{
            return getSharePrefs(context,PersistenceKeys.UserId.toString())
        }


        //USERNAME
        fun setUserName(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.UserName.toString(),value)
        }
        fun getUserName(context: Context):String{
            return getSharePrefs(context,PersistenceKeys.UserName.toString())
        }

        //USEREMAIL
        fun setUserEmail(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.UserEmail.toString(),value)
        }
        fun getUserEmail(context: Context):String{
            return getSharePrefs(context,PersistenceKeys.UserEmail.toString())
        }

        //USERIMAGE
        fun setUserImage(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.UserImage.toString(),value)
        }
        fun getUserImage(context: Context):String{
            return getSharePrefs(context,PersistenceKeys.UserImage.toString())
        }



    }
}