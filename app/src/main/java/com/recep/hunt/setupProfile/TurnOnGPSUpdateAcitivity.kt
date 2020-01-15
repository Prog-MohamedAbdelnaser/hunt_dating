package com.recep.hunt.setupProfile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.recep.hunt.R
import com.recep.hunt.setupProfile.exceptions.LocationServiceRequestException
import com.recep.hunt.setupProfile.exceptions.PermissionDeniedException
import com.recep.hunt.utilis.launchActivity
import com.recep.hunt.utilis.location.EnableLocationServiceSettingActivity
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleSource
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_turn_on_gps.*


/**
 * Created by Rishabh Shukla
 * on 2019-09-15
 * Email : rishabh1450@gmail.com
 */

class TurnOnGPSUpdateAcitivity : AppCompatActivity() {



    private var disposable: Disposable? = null

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
        if(checkPermissionForBtn()){ turn_on_gps_btn.text = resources.getString(R.string.continuee) }else{ turn_on_gps_btn.text = resources.getString(R.string.turn_on_gps) }

        turn_on_gps_btn.setOnClickListener {
            if(checkPermissionForBtn()){
                launchActivity<SetupProfileCompletedActivity>()
            }else{
                checkLocationSetting()
            }
        }

    }

    private fun checkLocationSetting() {
        disposable =  EnableLocationServiceSettingActivity.checkLocationServiceSetting(this)
            .subscribe({
                turn_on_gps_btn.text = resources.getString(R.string.continuee)
            }, {
                if (it is PermissionDeniedException) {
                    // todo if you need to show user popup with permission need description
                }
            })
    }

    private fun checkPermissionForBtn():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

}
