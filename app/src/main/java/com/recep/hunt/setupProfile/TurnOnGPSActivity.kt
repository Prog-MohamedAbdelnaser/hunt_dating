package com.recep.hunt.setupProfile

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.recep.hunt.R
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_turn_on_gps.*
import android.R.string.cancel
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.location.LocationManager




class TurnOnGPSActivity : AppCompatActivity() {

    companion object{
         const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var latitude = 0.toDouble()
    private var longitude = 0.toDouble()
    lateinit var mLastLocation : Location

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var locationCallback: LocationCallback
    private val TAG = TurnOnGPSActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_on_gps)
        init()
//        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))

    }

    private fun init() {
        setSupportActionBar(turn_on_gps_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        if(checkPermissionForBtn()){
            turn_on_gps_btn.text = resources.getString(R.string.continuee)
        }else{
            turn_on_gps_btn.text = resources.getString(R.string.turn_on_gps)
        }

        turn_on_gps_btn.setOnClickListener {
            val btnTitle = turn_on_gps_btn.text.toString()
            if(btnTitle == resources.getString(R.string.turn_on_gps)){
                showGPSDisabledAlertToUser()
            }else{
                launchActivity<SetupProfileCompletedActivity>()
            }
        }


    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Goto Settings Page To Enable GPS"
            ) { dialog, id ->
                val callGPSSettingIntent = Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                startActivity(callGPSSettingIntent)
            }
        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialog, _ -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    private fun buildLocationRequest(){

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.smallestDisplacement = 10f

    }
    private fun buildLocationCallBack(){
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                mLastLocation = p0!!.locations[p0.locations.size - 1]
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                SharedPrefrenceManager.setUserLatitude(this@TurnOnGPSActivity,latitude.toString())
                SharedPrefrenceManager.setUserLatitude(this@TurnOnGPSActivity,longitude.toString())

            }
        }
    }
    private fun turnOnGPS(){
        val provider = android.provider.Settings.Secure.getString(contentResolver,android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if(!provider.contains("gps")){
            val intent = Intent()
            intent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider")
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE)
            intent.setData(Uri.parse("3"))
            sendBroadcast(intent)
        }

    }
    private fun checkPermissionForBtn():Boolean{
//        return ActivityCompat.checkSelfPermission(this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermission()
                    buildLocationRequest()
                    buildLocationCallBack()
                    turnOnGPS()
                    init()
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

                }else{
                    Helpers.showErrorSnackBar(this,"Turn on Location!","Please allow for location")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
