package com.recep.hunt.home

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.orhanobut.logger.Logger
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.home.adapter.*
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.NearestLocation
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.nearestLocation.NearestLocationResponse
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.setupProfile.TurnOnGPSActivity
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.*
import com.recep.hunt.utilis.location.EnableLocationServiceSettingActivity
import com.squareup.moshi.Json
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_infowindow.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.layoutInflater
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt

class HomeActivity : AppCompatActivity(), OnMapReadyCallback,
    FilterBottomSheetDialog.FilterBottomSheetListener,
    PlacesAutoCompleteAdapter.ClickListener,
    NearByRestaurantsAdapter.NearByRestaurantsAdapterListener,
    FarAwayRestaurantsVerticalAdapter.FarAwayRestaurantsVerticalAdapterListener {

    private lateinit var mMap: GoogleMap

    companion object {

        const val PROVIDERS_CHANGED = "android.location.PROVIDERS_CHANGED"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val animateZoomTo = 13.0f
        var NEAREST_DISTANCE = 3000 // testing purpose
    }

    private val disposables = CompositeDisposable()

    private var gpsReceiver: GpsLocationReceiver? = null

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"
    private var latitude = 0.toDouble()
    private var longitude = 0.toDouble()
    lateinit var mLastLocation: Location
    private var mMarker: Marker? = null
    private lateinit var geoCoder: Geocoder
    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    //    private lateinit var allNearByRestaurantsModel: NearByRestaurantsModel
    private lateinit var showSortedListCardView: CardView
    private lateinit var showMyLocationCardView: CardView
    private lateinit var sortedListRecyclerView: RecyclerView
    private lateinit var locationButton: ImageView
    private lateinit var searchTextView: TextView
    private lateinit var placesRecyclerView: RecyclerView
    private var isListshowing = false
    private var adapter = GroupAdapter<ViewHolder>()
    private lateinit var autoCompleteAdapter: PlacesAutoCompleteAdapter
    private var callAPIOnlyOnceStatus = 1

    private val REQUEST_CODE_ASK_PERMISSIONS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Utils.placesApiError.observe(this, androidx.lifecycle.Observer {
            if (it != "false") {
                showErrorAlertToUser(it)
                Utils.placesApiError.value = "false"
            }
        })

        Utils.noUserError.observe(this, androidx.lifecycle.Observer {
            if (it != "false") {
                showNoUserBottomSheet()
                Utils.placesApiError.value = "false"
            }
        })

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
        } else {
            init()

        }
    }


    private fun init() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        MapsInitializer.initialize(this)
        val mapFrag = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFrag.getMapAsync(this)
        val mapFragView = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
        mapFragView.alpha = 0.95f
        locationButton = (mapFrag.view!!.find<View>(Integer.parseInt("1")).parent as View).findViewById(Integer.parseInt("2"))

        geoCoder = Geocoder(this, Locale.getDefault())

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, GOOGLE_API_KEY_FOR_IMAGE)
        }

        autoCompleteAdapter = PlacesAutoCompleteAdapter(this)

        searchTextView = find(R.id.textView6)
        showSortedListCardView = find(R.id.sorted_near_by_restaurants_list_card)
        showMyLocationCardView = find(R.id.my_location_crd)

        sortedListRecyclerView = find(R.id.sorted_near_by_restaurants_recyclerView)

        placesRecyclerView = find(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)
        placesRecyclerView.addItemDecoration(
            ListPaddingDecoration(
                placesRecyclerView.context,
                70,
                60
            )
        )
        autoCompleteAdapter.setClickListener(this)
        placesRecyclerView.adapter = autoCompleteAdapter
        autoCompleteAdapter.notifyDataSetChanged()


        sortedListRecyclerView.adapter = adapter
        sortedListRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

        val makeUserOnline = MakeUserOnline(true)

        val call = ApiClient.getClient.makeUserOnline(
            makeUserOnline,
            SharedPrefrenceManager.getUserToken(this)
        )

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@HomeActivity, strErrorJson)) {
                        return
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "You're online", Toast.LENGTH_SHORT).show()
                }

            }

        })

        setupCardClicks()

        setupToolbarClicks()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermission()

        buildLocationRequest()
        buildLocationCallBack()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        searchTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().equals("")) {
                    if (placesRecyclerView.visibility == View.GONE) {
                        placesRecyclerView.visibility = View.VISIBLE
                    }
                    autoCompleteAdapter.filter.filter(s.toString())
                } else {
                    placesRecyclerView.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

    }

    //PlacesAutoCompleteAdapter override
    override fun click(place: Place) {
        Utils.hideKeyboard(this@HomeActivity)
        place.latLng?.latitude?.let {
            place.latLng?.longitude?.let { it1 ->
                selectLocation(
                    it,
                    it1
                )
            }
        }
    }

    private fun selectLocation(latitude: Double, longitude: Double) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo), 3000, null)

        setPlaceRipple(LatLng(latitude, longitude))

        adapter.clear()
        nearestPlaces(latitude, longitude)
        searchTextView.text = ""
    }

    override fun onFilterBottomSheetClickApplyListener() {

        nearestPlaces(latitude,longitude)
    }


    fun isLocationEnabled()= EnableLocationServiceSettingActivity.isLocationEnabled(this)

    private fun showErrorAlertToUser(msg: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog.cancel()
            }
        val alert = alertDialogBuilder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Goto Settings Page To Enable GPS"
            ) { _, _ ->
                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, 0)
            }
        alertDialogBuilder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }


    private fun showIncognitoBtn() {
        val isIncognito = SharedPrefrenceManager.getisIncognito(this)
        if (isIncognito) {
            SharedPrefrenceManager.setisIncognito(this, false)
            val ll = LayoutInflater.from(this).inflate(R.layout.incoginito_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val gotItBtn: Button = dialog.find(R.id.got_it_btn)
            gotItBtn.setOnClickListener {
                home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost_on)
                makeUserOnOffline(false)
                dialog.dismiss()
            }
            dialog.show()
        } else {
            home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)
            makeUserOnOffline(true)
            SharedPrefrenceManager.setisIncognito(this, true)
        }

    }


    override fun onNearByRestaurantsClicked(position: Int) {
        Logger.d("nearby restaurant $position")
        launchActivity<SwipeMainActivity> { }
    }

//    private fun setupNearByRestaurantsRecyclerView(items: ArrayList<NearByRestaurantsModelResults>) {
//        horizontal_list_near_by_user.adapter = NearByRestaurantsAdapter(this,
//            items,
//            this)
//
//        horizontal_list_near_by_user.setOrientation(DSVOrientation.HORIZONTAL)
//        horizontal_list_near_by_user.setItemTransformer(ScaleTransformer.Builder()
//                .build())
//        horizontal_list_near_by_user.scrollToPosition(0)
//        horizontal_list_near_by_user.setSlideOnFling(true)
//    }

    //integration of nearest-place api

    fun setupNearByRestaurantsRecyclerViewByApiEmpty(){
        horizontal_list_near_by_user.adapter=null
        horizontal_list_near_by_user.removeAllViewsInLayout()
        horizontal_list_near_by_user.removeAllViews()
        sortedListRecyclerView.adapter=null
        sortedListRecyclerView.removeAllViewsInLayout()
        sortedListRecyclerView.removeAllViews()
        hideVerticallRecycler()



    }

    private fun nearestPlaces(lat: Double, long: Double) {
        val nearestLocation = NearestLocation(lat, long)
        val call = ApiClient.getClient.getNearestPlace(
            nearestLocation,
            SharedPrefrenceManager.getUserToken(this)
        )

        call.enqueue(object : Callback<NearestLocationResponse> {
            override fun onFailure(call: Call<NearestLocationResponse>, t: Throwable) {
                Log.d("Api call failure -> ", "" + call)

            }

            override fun onResponse(
                call: Call<NearestLocationResponse>,
                response: Response<NearestLocationResponse>
            ) {

                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@HomeActivity, strErrorJson)) {
                        return
                    }
                }

                var result = response.body()?.data
                result?.let {
                    if(it.isEmpty()){
                        setupNearByRestaurantsRecyclerViewByApiEmpty()
                        Utils.noUserError.postValue("true")
                        Toast.makeText(this@HomeActivity, "No locations found/possible google quota issue", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val nearbyItems = ArrayList<NearestLocationData>()
                        val farItems = ArrayList<NearestLocationData>()
                        for (i in 0 until result.size) {
                            //If it's near than 50m
                            if (result[i].distance.toFloat() <= NEAREST_DISTANCE) {

                                if (result[i].distance.toFloat() == 0f) {
//                                if (result[i].users != 0) {
                                    selectLocationAndGetUsersList(
                                        result[i].place_id,
                                        result[i].name
                                    )
//                                } else {
//                                    AlertDialogUtils.displayDialog(
//                                        this@HomeActivity,
//                                        getString(R.string.no_user_found)
//                                    )
//                                }
                                }
                                nearbyItems.add(result[i])
                            } else {
                                farItems.add(result[i])
                            }
                        }
                        setPlacesMarker(nearbyItems, R.drawable.close_rest_marker)
                        setPlacesMarker(farItems, R.drawable.far_rest_markers)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
//                    mMap.setMaxZoomPreference(animateZoomTo)
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo))
                        setupNearByRestaurantsRecyclerViewByApi(nearbyItems)
                        setupSortedListView(nearbyItems, farItems)
                    }
                }
            }

        })
    }

    private fun setPlaceRipple(latLng: LatLng) {
//        val mapRipple = MapRipple(mMap, latLng, this@HomeActivity)
//
//        mapRipple.withNumberOfRipples(4)
//        mapRipple.withStrokeColor(resources.getColor(R.color.map_ripple_color))
//        mapRipple.withStrokewidth(1)
//        mapRipple.withFillColor(resources.getColor(R.color.map_ripple_color))
//        mapRipple.withDistance(100.toDouble())      // 2000 metres radius
//        mapRipple.withRippleDuration(10000)    //12000ms
//        mapRipple.withTransparency(0.4f)
//        mapRipple.startRippleMapAnimation()
    }

    private fun setPlacesMarker(items: ArrayList<NearestLocationData>, resource: Int) {
        val markerOptions = MarkerOptions()
        for (i in 0 until items.size) {
            val googlePlace = items[i]
            val mLat = googlePlace.lat
            val mLong = googlePlace.lang
            val placeName = googlePlace.name
            val latLong = LatLng(mLat, mLong)

            markerOptions.position(latLong)
            markerOptions.title(placeName)

            markerOptions.snippet(i.toString())

            val marker = mMap.addMarker(markerOptions)
            marker?.setIcon(BitmapDescriptorFactory.fromResource(resource))
            marker.tag = items[i]
        }

    }

    private fun setupNearByRestaurantsRecyclerViewByApi(items: ArrayList<NearestLocationData>?) {
        val windowwidth = windowManager.defaultDisplay.width
        horizontal_list_near_by_user.x = (0 - windowwidth.toFloat() / 2.07).toFloat()
        horizontal_list_near_by_user.layoutParams.width = windowwidth + windowwidth / 3 + 150
//        horizontal_list_near_by_user.setOffscreenItems(items!!.size)
        horizontal_list_near_by_user.adapter = NearByRestaurantsAdapterByApi(this, items)
        horizontal_list_near_by_user.setOrientation(DSVOrientation.HORIZONTAL)
        horizontal_list_near_by_user.setItemTransformer(
            ScaleTransformer.Builder().setMaxScale(
                1.125f
            ).setMinScale(0.98f).setPivotX(Pivot.X.CENTER).setPivotY(Pivot.Y.BOTTOM).build()
        )
        horizontal_list_near_by_user.scrollToPosition(0)
        horizontal_list_near_by_user.setSlideOnFling(true)
    }

    private fun setupSortedListView(nearItems: ArrayList<NearestLocationData>?, farItems: ArrayList<NearestLocationData>?) {
        sortedListRecyclerView.adapter = adapter
        sortedListRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)


        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.near_by_locations)))

        for (i in 0 until nearItems!!.size) {
            adapter.add(NearByRestaurantsVerticalAdapterByAPi(this@HomeActivity, nearItems))
        }
        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.far_away)))

        for (i in 0 until farItems!!.size) {
            adapter.add(
                FarAwayRestaurantsVerticalAdapterByApi(this@HomeActivity, farItems, nearItems.size)
            )
        }
    }

//    private fun nearByRestaurants(
//        lat: Double,
//        long: Double,
//        radius: String,
//        completion: (APIState, ArrayList<NearByRestaurantsModelResults>?) -> Unit
//    ) {
//        val path = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
//        Log.e("Lat long", "$lat,$long")
//        val params =
//            "location=$lat,$long&radius=$radius&type=restaurant&key=${resources.getString(R.string.browser_key)}"
//        val serviceVolley = ServiceVolley()
//        val apiController = APIController(serviceVolley)
//        val url = "$path?$params"
//        Log.e("URL", "$url")
//        val dialog = Helpers.showDialog(this, this, "Getting near by restaurants").show()
//        apiController.get(path, params) { response ->
//            if (dialog != null) {
//                dialog.dismiss()
//            }
//            if(response != null){
//                allNearByRestaurantsModel = Gson().fromJson(response, NearByRestaurantsModel::class.java)
//                val results = allNearByRestaurantsModel.nearByRestaurantsModelResults
//                completion(APIState.SUCCESS, results)
//                setupSortedListRecyclerView(results)
//            }
//
//
//        }
//
//    }

//    private fun setupAllNearByRestMarkers(lat: Double, long: Double) {
//        nearByRestaurants(lat, long, "1000") { response, results ->
//            if (response == APIState.SUCCESS) {
//                val markerOptions = MarkerOptions()
//                if (results != null) {
//                    for (i in 0 until results.size - 1) {
//                        val googlePlace = results[i]
//                        val mLat = googlePlace.geometry.location.lat
//                        val mLong = googlePlace.geometry.location.lng
//                        val placeName = googlePlace.name
//                        val latLong = LatLng(mLat, mLong)
//
//                        markerOptions.position(latLong)
//                        markerOptions.title(placeName).icon(null)
//
//                        markerOptions.snippet( i.toString())
//
//                        val marker = mMap.addMarker(markerOptions)
//                        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.far_rest_markers))
//                        marker.showInfoWindow()
//                    }
//                    setupNearByRestaurantsRecyclerView(results)
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
//                    mMap.setMaxZoomPreference(animateZoomTo)
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo))
//                }
//            }
//
//        }
//
//        runOnUiThread{
//
//        }
//        Run.after(1000) {
//            setupNearByResUnder600M(lat, long)
//        }
//
//    }
//
//    private fun setupNearByResUnder600M(lat: Double, long: Double) {
//        nearByRestaurants(lat, long, "600") { response, results ->
//            if (response == APIState.SUCCESS) {
//                val markerOptions = MarkerOptions()
//                if (results != null) {
//                    for (i in 0 until results.size - 1) {
//                        val googlePlace = results[i]
//                        val mLat = googlePlace.geometry.location.lat
//                        val mLong = googlePlace.geometry.location.lng
//                        val placeName = googlePlace.name
//                        val latLong = LatLng(mLat, mLong)
//
//                        markerOptions.position(latLong)
//                        markerOptions.title(placeName).icon(null)
//
//                        markerOptions.snippet(i.toString())
//
//                        val marker = mMap.addMarker(markerOptions)
//                        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.close_rest_marker))
//
//
//                    }
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo))
//                }
//            }
//
//        }
//
//    }

    private fun setupCardClicks() {
        showMyLocationCardView.setOnClickListener {
            checkLocationSetting()
        }

        showSortedListCardView.setOnClickListener {
            if (!isListshowing) {
                showVerticallRecycler()
            } else {
                hideVerticallRecycler()
            }


        }
    }

    private fun showVerticallRecycler() {
        isListshowing = true
        sortedListRecyclerView.visibility = View.VISIBLE
        horizontal_list_near_by_user.visibility = View.INVISIBLE
        val mapFrag = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
        mapFrag.alpha = 0.15f
        list_image_view.image = resources.getDrawable(R.drawable.ic_street_view)
    }

    private fun hideVerticallRecycler() {
        isListshowing = false
        sortedListRecyclerView.visibility = View.INVISIBLE
        horizontal_list_near_by_user.visibility = View.VISIBLE
        val mapFrag = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
        mapFrag.alpha = 0.95f
        list_image_view.image = resources.getDrawable(R.drawable.ic_format_list)
    }


    override fun onFarAwayRestaurantClick(position: Int) {
        farLocationErrorPrompt()
    }

    //todo create seperate class for this or convert to lazy load
    private fun farLocationErrorPrompt() {
        val locationFarErrorDialog = Dialog(this)

        val ll = LayoutInflater.from(this).inflate(R.layout.far_away_dialog_layout, null)
        locationFarErrorDialog.setContentView(ll)
        locationFarErrorDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val gotItBtn: Button = locationFarErrorDialog.find(R.id.far_away_ok_btn)
        gotItBtn.setOnClickListener {
            locationFarErrorDialog.dismiss()
        }

        locationFarErrorDialog.show()
    }

    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 0.1f
//        locationRequest.numUpdates = 1

    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {

                super.onLocationResult(p0)
                mLastLocation = p0!!.locations[p0.locations.size - 1]
                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                var title = "Your Place"

                try {
                    val listAddresses = geoCoder.getFromLocation(latitude, longitude, 1)
                    if (listAddresses != null && listAddresses.size > 0) {
                        title = listAddresses.get(0).getAddressLine(0)
                    }
                } catch (e: IOException) {
//                    e.printStackTrace()
                }
                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(null)


//                mMarker = mMap.addMarker(markerOptions)
//                mMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_placeholder))

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo), 3000, null)
                mMap.setOnMarkerClickListener { p0 ->
                    if (p0?.position?.latitude != latitude && p0?.position?.longitude != longitude) {
                        p0?.showInfoWindow()
                    }
                    true
                }

                mMap.setOnMyLocationButtonClickListener {
                    selectLocation(latitude, longitude)
                    false
                }

                setPlaceRipple(LatLng(latitude, longitude))

                SharedPrefrenceManager.setUserLatitude(this@HomeActivity, mLastLocation.latitude.toString())

                SharedPrefrenceManager.setUserLongitude(this@HomeActivity, mLastLocation.longitude.toString())

                if (Helpers.isInternetConnection(this@HomeActivity)) {
                    if (callAPIOnlyOnceStatus == 1) {
                        val lat = mLastLocation.latitude
                        val long = mLastLocation.longitude
                        nearestPlaces(lat, long)
//                        setupAllNearByRestMarkers(lat, long)
                        callAPIOnlyOnceStatus = 0
                    }

                }

            }
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)

            return
        }else{
            listenToGPSChanges()
        }
    }


    override fun onPause() {
        super.onPause()
        makeUserOnOffline(false)
    }


    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {

            mMap = p0
            mMap.uiSettings.isCompassEnabled = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                }
            } else
                mMap.isMyLocationEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = false
            mMap.uiSettings.isMapToolbarEnabled = false
            locationButton.visibility = View.GONE
            mMap.setInfoWindowAdapter(CustomInfoWindowView(this))
            checkLocationSetting()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hunt_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupToolbarClicks() {
        home_filter_btn.setOnClickListener {
            val bottomSheet = FilterBottomSheetDialog(this)
            bottomSheet.show(supportFragmentManager, "FilterBottomSheetDialog")
        }

        home_incoginoti_btn.setOnClickListener {
            showIncognitoBtn()
        }

        home_notification_btn.setOnClickListener {
            launchActivity<NotificationsActivity>()
        }

        home_profile_btn.setOnClickListener {
            launchActivity<UserProfileActivity>()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            TurnOnGPSActivity.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermission()
                    buildLocationRequest()
                    buildLocationCallBack()
                    fusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                } else {
                    Helpers.showErrorSnackBar(
                        this,
                        "Turn on Location!",
                        "Please allow for location"
                    )
                }
            }
            REQUEST_CODE_ASK_PERMISSIONS -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    Helpers.showErrorSnackBar(
                        this,
                        "Please allow location access!",
                        "Please allow for location"
                    )

                }

            }


        }
    }

    private fun makeUserOnOffline(is_online: Boolean) {
        val makeUserOnline = MakeUserOnline(is_online)

        val call = ApiClient.getClient.makeUserOnline(
            makeUserOnline,
            SharedPrefrenceManager.getUserToken(this)
        )

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@HomeActivity, strErrorJson)) {
                        return
                    }
                }


                if (is_online == false)
                    Toast.makeText(this@HomeActivity, "You're offline", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@HomeActivity, "You're online", Toast.LENGTH_SHORT).show()
            }

        })

    }


    fun getUsersList(location_id: String,
        age: String,
        date: String,
        business: String,
        friendship: String,
        location_name: String
    ) {

        val lat = SharedPrefrenceManager.getUserLatitude(this)
        val lang = SharedPrefrenceManager.getUserLongitude(this)
        val filter = UsersListFilter(location_id, age, date, business, friendship, lat, lang)
//        val filter = UsersListFilter(
//            "ChIJDZPv6a8lv0cRBFRz6EJVlxY01",
//            "25,30",
//            "both",
//            "both",
//            "both",
//            "77.6722",
//            "12.8821"
//        )
        val call =
            ApiClient.getClient.usersList(filter, SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                Log.d("Api call failure -> ", "" + call)
            }

            override fun onResponse(
                call: Call<UsersListResponse>,
                response: Response<UsersListResponse>
            ) {

                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@HomeActivity, strErrorJson)) {
                        return
                    }
                }

                var result = response.body()?.data
                var swipeUserArray = ArrayList<SwipeUserModel>()
                if (result != null && result.size > 0) {
                    for (i in 0 until result.size) {
                        val images = ArrayList<String>()
                        if (result[i].user_profile_image.size != 0) {
                            for (j in 0 until result[i].user_profile_image.size) {
                                images.add(result[i].user_profile_image[j].image)
                            }
                        } else {
                            val baseUrl = "https://hunt.nyc3.digitaloceanspaces.com/User/"
                            images.add(baseUrl + result[i].profile_pic)
                        }
                        swipeUserArray.add(
                            SwipeUserModel(
                                result[i].id,
                                result[i].location_name,
                                result[i].email,
                                result[i].first_name,
                                result[i].age,
                                result[i].basicInfo.job_title,
                                result[i].basicInfo.about,
                                result[i].totalMatching,
                                result[i].totalMeeting.roundToInt(),
                                result[i].is_online,
                                result[i].for_date,
                                result[i].for_bussiness,
                                result[i].for_friendship,
                                images,
                                result[i].basicInfo
                            )
                        )
                    }
                    launchActivity<SwipeMainActivity> {
                        putParcelableArrayListExtra("swipeUsers", swipeUserArray)
                        putExtra(FeaturesConstants.LOCATION_OBJECT_KEY,location_name)

                    }
                } else {
                      launchActivity<SwipeMainActivity> {
                          putParcelableArrayListExtra("swipeUsers", swipeUserArray)
                          putExtra(FeaturesConstants.LOCATION_OBJECT_KEY,location_name)


                      }
                    //showNoUserBottomSheet()
                }
            }
        })
    }

    fun showNoUserBottomSheet() {
        var noUserbottomSheetFragment = NoUserBottomSheetFragment()
        noUserbottomSheetFragment.show(
            getSupportFragmentManager(),
            noUserbottomSheetFragment.tag
        )
    }

    fun selectLocationAndGetUsersList(location_id: String, location_name: String) {
        var leftAge = SharedPrefrenceManager.getUserInterestedAgeFrom(this)
        if (leftAge.isEmpty())
            leftAge = "18"
        var rightAge = SharedPrefrenceManager.getUserInterestedAgeTo(this)
        if (rightAge.isEmpty())
            rightAge = "50"
        val age = "$leftAge,$rightAge"
        var date = SharedPrefrenceManager.getUserLookingFor(this, "Date")
        var business = SharedPrefrenceManager.getUserLookingFor(this, "Business")
        var friendship = SharedPrefrenceManager.getUserLookingFor(this, "Friendship")
        date = date.toLowerCase()
        business = business.toLowerCase()
        friendship = friendship.toLowerCase()
        val location = SelectLocation(location_id, location_name)
        val call =
            ApiClient.getClient.selectLocation(location, SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<SelectLocationResponse> {
            override fun onFailure(call: Call<SelectLocationResponse>, t: Throwable) {
                Log.d("Api call failure -> ", "" + call)
            }

            override fun onResponse(
                call: Call<SelectLocationResponse>,
                response: Response<SelectLocationResponse>
            ) {


                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@HomeActivity, strErrorJson)) {
                        return
                    }
                }

                var result = response.body()?.data
                if (result != null) {
                    getUsersList(location_id, age, date, business, friendship,location_name)
//                        context.launchActivity<SwipeMainActivity> {  }
                }
            }

        })
    }

    private fun listenToGPSChanges() {
        this?.apply {
            gpsReceiver = GpsLocationReceiver()
            registerReceiver(gpsReceiver, IntentFilter(PROVIDERS_CHANGED))
        }
    }

    inner class GpsLocationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.w("GpsLocationReceiver", "onReceive ${intent.action}")
            if (intent.action?.equals(PROVIDERS_CHANGED) == true) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    onEnableGPS()
                } else {
                    onDisableGPS()
                    //showErrorSnackbar(contentView, getString(R.string.wont_detect_location))
                }
            }
        }
    }

    fun clickMyLocationButton(){
        locationButton.callOnClick()
        mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo), 3000, null)
    }

    private fun onDisableGPS() {
        checkLocationSetting()
    }

    private fun checkLocationSetting() {
        disposables.add(EnableLocationServiceSettingActivity.checkLocationServiceSetting(this).
            subscribe({
                if (it){
                    clickMyLocationButton()
                }
            },{
                it.printStackTrace()
            }))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (gpsReceiver != null) {
            unregisterReceiver(gpsReceiver)
            gpsReceiver = null
        }
        if (disposables.isDisposed.not())
            disposables.dispose()
    }

    private fun onEnableGPS() {

    }
}

class CustomInfoWindowView(val context: Context) : GoogleMap.InfoWindowAdapter {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"

    override fun getInfoWindow(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_infowindow, null)
        view.alpha = 1.0f
        if (marker != null) {
            view.info_window_rest_name.text = marker.title
            marker.setInfoWindowAnchor(7f,0f)
            if (marker.tag != null) {
                val locationInfo = marker.tag as NearestLocationData

                if(locationInfo.distance.roundToInt() > HomeActivity.NEAREST_DISTANCE ){
                    view.textView30.setTextColor(ActivityCompat.getColor(context, R.color.font_color_far_location))
                    view.textView31.setTextColor(ActivityCompat.getColor(context, R.color.font_color_far_location))
                    view.image_view_location_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_far_away_distance))
                    view.image_view_user_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_far_away_rest_user))
                }
                else{
                    view.textView30.setTextColor(ActivityCompat.getColor(context, R.color.font_color_near_location))
                    view.textView31.setTextColor(ActivityCompat.getColor(context, R.color.font_color_near_location))
                    view.image_view_location_icon.setColorFilter(ActivityCompat.getColor(context, R.color.font_color_near_location), android.graphics.PorterDuff.Mode.MULTIPLY)
                    view.image_view_user_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_near_by_rest_user))
                }

                view.textView30.text = (locationInfo.users).toString()

                if (locationInfo.distance < 1000) {
                    view.textView31.setText("${locationInfo.distance.roundToInt()} M")
                }
                else{
                    view.textView31.setText("${String.format("%.2f", locationInfo.distance/1000)} KM")
                }

                val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${locationInfo.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"
                Logger.d("url = $url")
                Glide.with(context)
                    .load(url)
                    .transform(RoundedCorners(20))
                    .placeholder(R.drawable.ic_img_location_placeholder)
                    .into(view.info_window_rest_image)

            } else {
                view.textView30.text = 0.toString()
                view.textView31.text = "0 M"
            }
        }
        return view
    }

    //getInfoWindow
    override fun getInfoContents(p0: Marker?) = null

}

class ListPaddingDecoration(context: Context, val paddingLeft: Int, val paddingRight: Int) :
    RecyclerView.ItemDecoration() {
    private var mDivider: Drawable? = null

    init {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + paddingLeft
        val right = parent.width - parent.paddingRight - paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)

            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }
        }
    }
}

