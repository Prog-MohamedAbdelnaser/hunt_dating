package com.recep.hunt.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsy.maps_library.MapRipple
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.home.adapter.NearByRestaurantsAdapter
import com.recep.hunt.home.adapter.NearByRestaurantsVerticalAdapter
import com.recep.hunt.home.adapter.SimpleHeaderItemAdapter
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.home.adapter.FarAwayRestaurantsVerticalAdapter
import com.recep.hunt.setupProfile.TurnOnGPSActivity
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModel
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.utilis.*
import com.recep.hunt.volleyHelper.APIController
import com.recep.hunt.volleyHelper.APIState
import com.recep.hunt.volleyHelper.ServiceVolley
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_turn_on_gps.*
import kotlinx.android.synthetic.main.custom_infowindow.view.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, FilterBottomSheetDialog.FilterBottomSheetListener {


    override fun onOptionClick(text: String) {
    }

    private lateinit var mMap: GoogleMap

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val animateZoomTo = 17.0f
    }

    private var latitude = 0.toDouble()
    private var longitude = 0.toDouble()
    lateinit var mLastLocation: Location
    private var mMarker: Marker? = null
    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var allNearByRestaurantsModel: NearByRestaurantsModel
    private lateinit var showSortedListCardView: CardView
    private lateinit var showMyLocationCardView: CardView
    private lateinit var sortedListRecyclerView: RecyclerView
    private var isListshowing = true
    private var adapter = GroupAdapter<ViewHolder>()
    private var callAPIOnlyOnceStatus = 1
    private lateinit var locationButton: ImageView
    private val REQUEST_CODE_ASK_PERMISSIONS=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat
                .requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_ASK_PERMISSIONS);
        }
        else{
            init()

        }
    }


    private fun init() {
        MapsInitializer.initialize(this)
        val mapFrag = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFrag.getMapAsync(this)
        val mapFragView = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
        mapFragView.alpha = 0.7f
        locationButton = (mapFrag.view!!.find<View>(Integer.parseInt("1")).parent as View)
            .findViewById(Integer.parseInt("2"))

        showSortedListCardView = find(R.id.sorted_near_by_restaurants_list_card)
        showMyLocationCardView = find(R.id.my_location_crd)

        sortedListRecyclerView = find(R.id.sorted_near_by_restaurants_recyclerView)

        sortedListRecyclerView.adapter = adapter
        sortedListRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

        val makeUserOnline=MakeUserOnline(true)

        val call = ApiClient.getClient.makeUserOnline(makeUserOnline,SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object :Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
              Toast.makeText(this@HomeActivity,"Your online",Toast.LENGTH_SHORT).show()
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

        search_bar_layout.setOnClickListener {
            toast("Search")
        }

    }
    private fun showIncognitoBtn() {
        val isIncognito = SharedPrefrenceManager.getisIncognito(this)
        if(isIncognito){
            SharedPrefrenceManager.setisIncognito(this,false)
            val ll = LayoutInflater.from(this).inflate(R.layout.incoginito_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val gotItBtn: Button = dialog.find(R.id.got_it_btn)
            gotItBtn.setOnClickListener {
                home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost_on)
                makeUserOfline()
                dialog.dismiss()
            }
            dialog.show()
        }else{
            home_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)
            SharedPrefrenceManager.setisIncognito(this,true)
        }

    }


    private fun setupNearByRestaurantsRecyclerView(items: ArrayList<NearByRestaurantsModelResults>) {
        horizontal_list_near_by_user.adapter = NearByRestaurantsAdapter(this, items)
        horizontal_list_near_by_user.setOrientation(DSVOrientation.HORIZONTAL)
        horizontal_list_near_by_user.setItemTransformer(ScaleTransformer.Builder()
                .build())
        horizontal_list_near_by_user.scrollToPosition(0)
        horizontal_list_near_by_user.setSlideOnFling(true)
    }

    private fun nearByRestaurants(
        lat: Double,
        long: Double,
        radius: String,
        completion: (APIState, ArrayList<NearByRestaurantsModelResults>?) -> Unit
    ) {
        val path = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
        Log.e("Lat long", "$lat,$long")
        val params =
            "location=$lat,$long&radius=$radius&type=restaurant&key=${resources.getString(R.string.browser_key)}"
        val serviceVolley = ServiceVolley()
        val apiController = APIController(serviceVolley)
        val url = "$path?$params"
        Log.e("URL", "$url")
        val dialog = Helpers.showDialog(this, this, "Getting near by restaurants").show()
        apiController.get(path, params) { response ->
            if (dialog != null) {
                dialog.dismiss()
            }
            if(response != null){
                allNearByRestaurantsModel = Gson().fromJson(response, NearByRestaurantsModel::class.java)
                val results = allNearByRestaurantsModel.nearByRestaurantsModelResults
                completion(APIState.SUCCESS, results)
                setupSortedListRecyclerView(results)
            }


        }

    }

    private fun setupAllNearByRestMarkers(lat: Double, long: Double) {
        nearByRestaurants(lat, long, "1000") { response, results ->
            if (response == APIState.SUCCESS) {
                val markerOptions = MarkerOptions()
                if (results != null) {
                    for (i in 0 until results.size - 1) {
                        val googlePlace = results[i]
                        val mLat = googlePlace.geometry.location.lat
                        val mLong = googlePlace.geometry.location.lng
                        val placeName = googlePlace.name
                        val latLong = LatLng(mLat, mLong)

                        markerOptions.position(latLong)
                        markerOptions.title(placeName).icon(null)

                        markerOptions.snippet(i.toString())

                        val marker = mMap.addMarker(markerOptions)
                        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.far_rest_markers))
                        marker.showInfoWindow()
                    }
                    setupNearByRestaurantsRecyclerView(results)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
                    mMap.setMaxZoomPreference(animateZoomTo)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo))
                }
            }

        }

        runOnUiThread{

        }
        Run.after(1000) {
            setupNearByResUnder600M(lat, long)
        }

    }

    private fun setupNearByResUnder600M(lat: Double, long: Double) {
        nearByRestaurants(lat, long, "600") { response, results ->
            if (response == APIState.SUCCESS) {
                val markerOptions = MarkerOptions()
                if (results != null) {
                    for (i in 0 until results.size - 1) {
                        val googlePlace = results[i]
                        val mLat = googlePlace.geometry.location.lat
                        val mLong = googlePlace.geometry.location.lng
                        val placeName = googlePlace.name
                        val latLong = LatLng(mLat, mLong)

                        markerOptions.position(latLong)
                        markerOptions.title(placeName).icon(null)

                        markerOptions.snippet(i.toString())

                        val marker = mMap.addMarker(markerOptions)
                        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.close_rest_marker))


                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo))
                }
            }

        }

    }

    private fun setupCardClicks() {
        showMyLocationCardView.setOnClickListener {
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo),3000,null)
            locationButton.callOnClick()


        }

        showSortedListCardView.setOnClickListener {
            if (isListshowing) {
                isListshowing = false

                sortedListRecyclerView.visibility = View.VISIBLE
                horizontal_list_near_by_user.visibility = View.INVISIBLE
                val mapFrag = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
                mapFrag.alpha = 0.15f
                list_image_view.image = resources.getDrawable(R.drawable.ic_street_view)
//                home_root_view.backgroundColor = Color.parseColor("#CCFFFFFF")

            } else {
                isListshowing = true
                sortedListRecyclerView.visibility = View.INVISIBLE
                horizontal_list_near_by_user.visibility = View.VISIBLE
                val mapFrag = supportFragmentManager.findFragmentById(R.id.maps)?.view as View
                mapFrag.alpha = 0.7f
                list_image_view.image = resources.getDrawable(R.drawable.ic_format_list)

            }


        }
    }

    private fun setupSortedListRecyclerView(items: ArrayList<NearByRestaurantsModelResults>) {
        adapter.setOnItemClickListener { item, view ->
            val ll = LayoutInflater.from(this).inflate(R.layout.far_away_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val gotItBtn: Button = dialog.find(R.id.far_away_ok_btn)
            gotItBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        adapter.clear()

        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.near_by_locations)))
        //todo confirm if there is really a limit
        for (i in 0 until 3) {
            adapter.add(
                NearByRestaurantsVerticalAdapter(
                    this@HomeActivity,
                    items
                )
            )
        }
        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.far_away)))
        for (i in 4 until 6) {
            adapter.add(
                FarAwayRestaurantsVerticalAdapter(
                    this@HomeActivity,
                    items
                )
            )
        }
//        for (i in 4 until items.size) {
//            adapter.add(
//                FarAwayRestaurantsVerticalAdapter(
//                    this@HomeActivity,
//                    items
//                )
//            )
//        }

    }

    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.smallestDisplacement = 10f
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


                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .icon(null)


                mMarker = mMap.addMarker(markerOptions)
//                mMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_placeholder))

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(animateZoomTo),3000,null)

//                mapRipple = MapRipple(mMap, LatLng(mLastLocation.latitude, mLastLocation.longitude), this@HomeActivity)
//
//                mapRipple.withNumberOfRipples(4)
//                mapRipple.withStrokeColor(resources.getColor(R.color.map_ripple_color))
//                mapRipple.withStrokewidth(1)
//                mapRipple.withFillColor(resources.getColor(R.color.map_ripple_color))
//                mapRipple.withDistance(100.toDouble())      // 2000 metres radius
//                mapRipple.withRippleDuration(10000)    //12000ms
//                mapRipple.withTransparency(0.4f)
//                mapRipple.startRippleMapAnimation()

                SharedPrefrenceManager.setUserLatitude(this@HomeActivity, mLastLocation.latitude.toString())
                SharedPrefrenceManager.setUserLongitude(this@HomeActivity, mLastLocation.longitude.toString())

                if (Helpers.isInternetConnection(this@HomeActivity)) {
                    if(callAPIOnlyOnceStatus == 1){
//                        val lat = mLastLocation.latitude
//                        val long = mLastLocation.longitude
                        val lat =  mLastLocation.latitude
                        val long = mLastLocation.longitude
                        setupAllNearByRestMarkers(lat, long)
                        callAPIOnlyOnceStatus = 0
                    }

                }


            }
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }


    override fun onPause() {
        super.onPause()
        makeUserOfline()
    }


    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {

            mMap = p0
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            TurnOnGPSActivity.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermission()
                    buildLocationRequest()
                    buildLocationCallBack()
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                } else {
                    Helpers.showErrorSnackBar(this, "Turn on Location!", "Please allow for location")
                }
            }
            REQUEST_CODE_ASK_PERMISSIONS->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    init()
                }
                else{
                    Helpers.showErrorSnackBar(this, "Please allow location access!", "Please allow for location")

                }

            }


        }
    }

    fun makeUserOfline()
    {
        val makeUserOnline=MakeUserOnline(false)

        val call = ApiClient.getClient.makeUserOnline(makeUserOnline,SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object :Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {
                Toast.makeText(this@HomeActivity,"Your online",Toast.LENGTH_SHORT).show()
            }

        })

    }

}

class CustomInfoWindowView(val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker?): View {
        val view = context.layoutInflater.inflate(R.layout.custom_infowindow, null)
        if (marker != null) {
            view.info_window_rest_name.text = marker.title
        }
        return view
    }

    //getInfoWindow
    override fun getInfoContents(p0: Marker?) = null

}