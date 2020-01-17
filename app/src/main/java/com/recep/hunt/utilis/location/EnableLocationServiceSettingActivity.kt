package com.recep.hunt.utilis.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentSender
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.recep.hunt.R
import com.recep.hunt.setupProfile.exceptions.LocationServiceRequestException
import com.recep.hunt.setupProfile.exceptions.PermissionDeniedException
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.SingleEmitter
import io.reactivex.SingleSource
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_turn_on_gps.*

class EnableLocationServiceSettingActivity: AppCompatActivity() {


    companion object{

        private var emitter: SingleEmitter<Boolean>? = null
        private var singleEmitter: Single<Boolean>? = null

        const val LOCATION_SERVICE_REQUEST_CODE = 1000

        const val PROVIDERS_CHANGED = "android.location.PROVIDERS_CHANGED"

        fun checkLocationServiceSetting(context: Context): Single<Boolean> {
            if (isLocationEnabled(context)) {
                println("check location is enabled ")
                return Single.just(true)
            } else {
                println("will startCheckLocationServiceSettingActivity ")
                val v = Single.create<Boolean> { emitter -> EnableLocationServiceSettingActivity.emitter = emitter }
                startCheckLocationServiceSettingActivity(context)

                return v!!
            }
        }

        private fun startCheckLocationServiceSettingActivity(context: Context) {

            val intent = Intent(context, EnableLocationServiceSettingActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun isLocationEnabled(context: Context): Boolean {
            println("EnableGpsService isLocationEnabled ")
            val locationMode: Int
            val locationProviders: String
            if (isKitkatOrLater()) {
                try {
                    locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
                    return locationMode != Settings.Secure.LOCATION_MODE_OFF
                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                    return false
                }

            } else {
                locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }

        private fun isKitkatOrLater(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        }

    }


    private var disposable: Disposable? = null

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private var permissionEmitter: SingleEmitter<Boolean>? = null

    private var locationServiceEmitter: SingleEmitter<Boolean>? = null

    private val locationPermissionRequestCode = 1500

    private var locationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationSetting()
    }

    private fun checkLocationSetting() {
        disposable = checkPermission()
            .flatMap(requestLocationPermissionFunction())
            .flatMap(requestLocationServiceSettingFunction())
            .doFinally {
                finish()
            }
            .subscribe({
                emitter?.onSuccess(true)
            }, {
                emitter?.onError(it)
            })
    }

    private fun checkLocationSettingSingle():Single<Boolean> {
        return checkPermission()
            .flatMap(requestLocationPermissionFunction())
            .flatMap(requestLocationServiceSettingFunction())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                permissionEmitter?.apply {
                    if (!isDisposed) {
                        onSuccess(true)
                        permissionEmitter = null
                    }
                }
            } else {
                permissionEmitter?.apply {
                    if (!isDisposed) {
                        onError(PermissionDeniedException())
                        permissionEmitter = null
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //onResume()
        if (requestCode == LOCATION_SERVICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                locationServiceEmitter?.apply {
                    if (!isDisposed) {
                        onSuccess(true)
                        locationServiceEmitter = null
                    }
                }
            } else {
                locationServiceEmitter?.apply {
                    if (!isDisposed) {
                        finish()
                        onError(LocationServiceRequestException())
                        locationServiceEmitter = null

                    }
                }
            }
        }
    }

    private fun checkPermission(): Single<Boolean> =
        if (isMarshmallowOrLater()) {
            Single.just(isLocationPermissionGranted())
        } else {
            Single.just(true)
        }

    private fun isMarshmallowOrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun isLocationPermissionGranted() =
        checkSelfPermission() == PermissionChecker.PERMISSION_GRANTED

    private fun checkSelfPermission() =
        ContextCompat.checkSelfPermission(this, locationPermission)

    private fun requestLocationPermissionFunction() =
        Function<Boolean, SingleSource<Boolean>> { granted ->
            if (!granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestLocationPermissionSingle()
                } else {
                    Single.just(true)
                }
            } else {
                Single.just(true)
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermissionSingle(): Single<Boolean> {
        return Single.create<Boolean> {
            permissionEmitter = it
            requestPermissions(arrayOf(locationPermission), locationPermissionRequestCode)
        }
    }

    private fun requestLocationServiceSettingFunction() =
        Function<Boolean, SingleSource<Boolean>> { granted ->
            if (granted) {
                requestLocationServiceSettingSingle()
            } else {
                Single.error(IllegalArgumentException("Permissions must be granted first"))
            }
        }


    private fun requestLocationServiceSettingSingle(): Single<Boolean>? {
        return Single.create { emitter ->

            if (locationRequest == null) {
                locationRequest = createLocationRequest()
            }

            val locationSettingRequest =
                locationSettingsRequest(locationRequest!!)

            val settingsClient = LocationServices.getSettingsClient(this)

            val task: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(locationSettingRequest)

            task.addOnSuccessListener {
                emitter.onSuccess(true)
            }

            task.addOnFailureListener {
                if (it is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult()
                        val resolvable: ResolvableApiException = it
                        locationServiceEmitter = emitter
                        resolvable.startResolutionForResult(this,
                            LOCATION_SERVICE_REQUEST_CODE
                        )

                    } catch (sendEx: IntentSender.SendIntentException) {
                        emitter.onError(sendEx)
                    }
                } else {
                    emitter.onError(it)
                }
            }

            task.addOnCanceledListener {
                emitter.onError(LocationServiceRequestException())
            }
        }
    }


    private fun locationSettingsRequest(locationRequest: LocationRequest) =
        LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)
            .build()

    private fun createLocationRequest() =
        LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setFastestInterval(5000)
            .setInterval(8000)
            .setSmallestDisplacement(5f)



}