package com.recep.hunt.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsy.maps_library.MapRipple
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.recep.hunt.R
import com.recep.hunt.home.adapter.NearByRestaurantsAdapter
import com.recep.hunt.home.adapter.NearByRestaurantsVerticalAdapter
import com.recep.hunt.home.adapter.SimpleHeaderItemAdapter
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.setupProfile.TurnOnGPSActivity
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModel
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.Run
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.recep.hunt.volleyHelper.APIController
import com.recep.hunt.volleyHelper.APIState
import com.recep.hunt.volleyHelper.ServiceVolley
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_turn_on_gps.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, FilterBottomSheetDialog.FilterBottomSheetListener {

    override fun onOptionClick(text: String) {

    }

    private lateinit var toolbar: Toolbar
    private lateinit var mMap : GoogleMap
    private lateinit var mapRipple:MapRipple

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private var latitude = 0.toDouble()
    private var longitude = 0.toDouble()
    lateinit var mLastLocation : Location
    private var mMarker : Marker? = null
    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var allNearByRestaurantsModel : NearByRestaurantsModel
    private lateinit var showSortedListCardView : CardView
    private lateinit var showMyLocationCardView : CardView
    private lateinit var sortedListRecyclerView : RecyclerView
    private var isListshowing = true
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }
    override fun onStop() {
        super.onStop()
        if (mapRipple.isAnimationRunning) {
            mapRipple.stopRippleMapAnimation()
        }
    }

    private fun init(){
        MapsInitializer.initialize(this)
        val mapFrag = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFrag.getMapAsync(this)

        toolbar = find(R.id.home_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        showSortedListCardView = find(R.id.sorted_near_by_restaurants_list_card)
        showMyLocationCardView = find(R.id.my_location_crd)
        sortedListRecyclerView = find(R.id.sorted_near_by_restaurants_recyclerView)

        setupCardClicks()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermission()
        buildLocationRequest()
        buildLocationCallBack()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

    }



    private fun setupNearByRestaurantsRecyclerView(items:ArrayList<NearByRestaurantsModelResults>){
        horizontal_list_near_by_user.adapter =
            NearByRestaurantsAdapter(this, items)
        horizontal_list_near_by_user.setOrientation(DSVOrientation.HORIZONTAL)
        horizontal_list_near_by_user.setItemTransformer(
            ScaleTransformer.Builder()
            .setMinScale(0.8f)
            .build())
    }

    private fun nearByRestaurants(lat:Double,long:Double,radius:String,completion:(APIState,ArrayList<NearByRestaurantsModelResults>?)->Unit){
        val path = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
        Log.e("Lat long","$lat,$long")
        val params ="location=$lat,$long&radius=$radius&type=restaurant&key=${resources.getString(R.string.browser_key)}"
        val serviceVolley = ServiceVolley()
        val apiController = APIController(serviceVolley)
        val url = "$path?$params"
        Log.e("URL","$url")
        val dialog = Helpers.showDialog(this,this,"Getting near by restaurants").show()
        apiController.get(path,params){response ->
            dialog.dismiss()
            allNearByRestaurantsModel = Gson().fromJson(response, NearByRestaurantsModel::class.java)
            val results = allNearByRestaurantsModel.nearByRestaurantsModelResults
                completion(APIState.Success,results)

            setupSortedListRecyclerView(results)


//            if(forNearBy){
////                nearByRestaurantsAdapter.setItems(results)
////                setupNearByRestaurantsRecyclerView(results)
//            }
////            completion(APIState.Success,results)
//            val markerOptions=MarkerOptions()
//            for(i in 0 until results.size - 1){
//                val googlePlace = results[i]
//                val mLat = googlePlace.geometry.location.lat
//                val mLong = googlePlace.geometry.location.lng
//                val placeName = googlePlace.name
//                val latLong = LatLng(mLat,mLong)
//
//                markerOptions.position(latLong)
//                markerOptions.title(placeName)
////                markerOptions.icon(BitmapDescriptorFactory.fromRxesource(R.drawable.ic_launcher_foreground))
//                markerOptions.snippet(i.toString())
//
//                mMap.addMarker(markerOptions)
//
//            }
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat,long)))
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
//

        }

    }

    private fun setupCardClicks(){
        showMyLocationCardView.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude,longitude)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
        }

        showSortedListCardView.setOnClickListener {
            if(isListshowing){
                isListshowing = false
                sortedListRecyclerView.visibility = View.VISIBLE
                horizontal_list_near_by_user.visibility = View.INVISIBLE
                Helpers.runAnimation(sortedListRecyclerView)
            }else{
                isListshowing = true
                Helpers.runReverseAnimation(sortedListRecyclerView)

                Run.after(705) {
                    sortedListRecyclerView.visibility = View.INVISIBLE
                    horizontal_list_near_by_user.visibility = View.VISIBLE
                }

            }


        }
    }


    private fun setupSortedListRecyclerView(items:ArrayList<NearByRestaurantsModelResults>){
        sortedListRecyclerView.adapter = adapter
        sortedListRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.near_by_locations)))
        for(i in 0 until 2){
            adapter.add(
                NearByRestaurantsVerticalAdapter(
                    this@HomeActivity,
                    items
                )
            )
        }
        adapter.add(SimpleHeaderItemAdapter(resources.getString(R.string.far_away)))
        for(i in 3 until items.size-1){
            adapter.add(
                NearByRestaurantsVerticalAdapter(
                    this@HomeActivity,
                    items
                )
            )
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
                if(mMarker != null){
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude


                val latLng = LatLng(latitude,longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .icon(null)


                mMarker = mMap.addMarker(markerOptions)

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

                mapRipple = MapRipple(mMap, LatLng(mLastLocation.latitude,mLastLocation.longitude),this@HomeActivity)

                    mapRipple.withNumberOfRipples(4)
                    mapRipple.withFillColor(resources.getColor(R.color.map_ripple_color))
                    mapRipple.withDistance(5500.toDouble())      // 2000 metres radius
                    mapRipple.withRippleDuration(12000)    //12000ms
                    mapRipple.withTransparency(0.5f)
                mapRipple.startRippleMapAnimation()


                SharedPrefrenceManager.setUserLatitude(this@HomeActivity,mLastLocation.latitude.toString())
                SharedPrefrenceManager.setUserLatitude(this@HomeActivity,mLastLocation.longitude.toString())

                if(Helpers.isInternetConnection(this@HomeActivity)){
                    nearByRestaurants(mLastLocation.latitude,mLastLocation.longitude,"1000"){res,data->
                        if(data != null)
                            setupNearByRestaurantsRecyclerView(data)
                        else
                            toast("No Restaurants Found")

                    }
                }


            }
        }
    }
    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if(p0 != null){

            mMap = p0
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mMap.isMyLocationEnabled = true
                }
            }else
                mMap.isMyLocationEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = false
            mMap.uiSettings.isMapToolbarEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false


        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hunt_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.profile_item -> launchActivity<UserProfileActivity>()
                R.id.filter_item->{
                    val bottomSheet = FilterBottomSheetDialog(this)
                    bottomSheet.show(supportFragmentManager,"FilterBottomSheetDialog")
                }
                R.id.notify_item->launchActivity<NotificationsActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            TurnOnGPSActivity.LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermission()
                    buildLocationRequest()
                    buildLocationCallBack()
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
                    turn_on_gps_btn.text = resources.getString(R.string.continuee)
                }else{
                    Helpers.showErrorSnackBar(this,"Turn on Location!","Please allow for location")
                }
            }
        }
    }
}


