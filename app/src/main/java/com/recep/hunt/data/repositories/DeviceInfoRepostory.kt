package com.recep.hunt.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.recep.hunt.utilis.PersistenceKeys
import com.recep.hunt.utilis.SharedPrefrenceManager

class DeviceInfoRepostory( private val context: Context) {


    private fun setDeviceInfo(deviceID: String) { SharedPrefrenceManager.setDeviceID(context, deviceID) }

    fun getDeviceID(): String {

        val deviceId=  SharedPrefrenceManager.getDeviceID(context)
        if (deviceId=="null") return getDeviceInfoForSetting() else return deviceId
    }

    @SuppressLint("MissingPermission")
    fun getDeviceInfoForSetting(): String {

        val tm =context. getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tm.getImei()
        }else
          tm.deviceId

        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val deviceModelNumber= Build.MODEL

       // val deviceInfo =DeviceInfo(androidId, deviceModelNumber, imei)
        setDeviceInfo(androidId)
        return androidId
    }

}