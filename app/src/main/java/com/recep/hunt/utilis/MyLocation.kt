package com.recep.hunt.utilis

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import java.lang.Exception
import java.util.*
import android.os.Bundle
import android.R.string.cancel
import android.R.string.cancel






/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class MyLocation {
//    lateinit var timer1:Timer
//    lateinit var lm : LocationManager
//    var gps_enabled = false
//    var network_enabled = false
//    lateinit var locationResult:LocationResult
//    private val TAG = MyLocation::class.java.simpleName
//
//    public fun getLocation(context: Context,res:LocationResult):Boolean{
//        locationResult=res
//            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        try{
//            gps_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        }catch (e:Exception){
//            log(e.toString())
//        }
//
//        if(!gps_enabled && !network_enabled)
//            return false
//
////        if(gps_enabled)
//
//
////        if (gps_enabled)
////            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps)
////        if (network_enabled)
////            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork)
////        timer1 = Timer()
////        timer1.schedule(GetLastLocation(), 20000)
//        return true
////        if(gps_enabled)
//
//
//    }
//
//    var locationListenerGps: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            timer1.cancel()
//            locationResult.gotLocation(location)
//            lm.removeUpdates(this)
//            lm.removeUpdates(locationListenerNetwork)
//        }
//
//        override fun onProviderDisabled(provider: String) {}
//        override fun onProviderEnabled(provider: String) {}
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//    }
//
//
//    var locationListenerNetwork: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            timer1.cancel()
//            locationResult.gotLocation(location)
//            lm.removeUpdates(this)
//            lm.removeUpdates(locationListenerGps)
//        }
//
//        override fun onProviderDisabled(provider: String) {}
//        override fun onProviderEnabled(provider: String) {}
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//    }
//
//    internal inner class GetLastLocation : TimerTask() {
//        override fun run() {
//            lm.removeUpdates(locationListenerGps)
//            lm.removeUpdates(locationListenerNetwork)
//
//            var net_loc: Location? = null
//            var gps_loc: Location? = null
//            if (gps_enabled)
//                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            if (network_enabled)
//                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//            //if there are both values use the latest one
//            if (gps_loc != null && net_loc != null) {
//                if (gps_loc.time > net_loc.time)
//                    locationResult.gotLocation(gps_loc)
//                else
//                    locationResult.gotLocation(net_loc)
//                return
//            }
//
//            if (gps_loc != null) {
//                locationResult.gotLocation(gps_loc)
//                return
//            }
//            if (net_loc != null) {
//                locationResult.gotLocation(net_loc)
//                return
//            }
//            locationResult.gotLocation(null)
//        }
//    }
//
//
//    private fun log(msg:String){
//        Log.e(TAG,msg)
//    }
//    abstract class LocationResult {
//        abstract fun gotLocation(location: Location)
//    }
}