package com.recep.hunt.setupProfile

import android.content.pm.PackageManager
import android.location.Location
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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    checkPermission()
            }else{
                launchActivity<SetupProfileCompletedActivity>()
            }
        }

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
    private fun checkPermissionForBtn():Boolean{
        return ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
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
