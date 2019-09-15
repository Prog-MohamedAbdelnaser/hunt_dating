package com.recep.hunt.setupProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_turn_on_gps.*


/**
 * Created by Rishabh Shukla
 * on 2019-09-15
 * Email : rishabh1450@gmail.com
 */

class TurnOnGPSUpdateAcitivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_on_gps)
        init()

    }

    private fun init() {
        setSupportActionBar(turn_on_gps_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        setupTurnOnGPS()

    }

    override fun onResume() {
        super.onResume()
        setupTurnOnGPS()

    }
    private fun setupTurnOnGPS(){
        if(checkPermissionForBtn()){
            turn_on_gps_btn.text = resources.getString(R.string.continuee)
        }else{
            turn_on_gps_btn.text = resources.getString(R.string.turn_on_gps)
        }
//
        turn_on_gps_btn.setOnClickListener {
            val btnTitle = turn_on_gps_btn.text.toString()
            if(btnTitle == resources.getString(R.string.turn_on_gps)){
                showGPSDisabledAlertToUser()
            }else{
                launchActivity<SetupProfileCompletedActivity>()
            }
        }

    }
    private fun checkPermissionForBtn():Boolean{
//        return ActivityCompat.checkSelfPermission(this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Goto Settings Page To Enable GPS"
            ) { dialog, id ->
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(callGPSSettingIntent)
                startActivityForResult(callGPSSettingIntent,0)
            }
        alertDialogBuilder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onResume()

    }
}
