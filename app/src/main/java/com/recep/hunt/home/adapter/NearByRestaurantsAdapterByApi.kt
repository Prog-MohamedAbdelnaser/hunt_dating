package com.recep.hunt.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.Helpers
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.utilis.launchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Rishabh Shukla
 * on 2019-08-27
 * Email : rishabh1450@gmail.com
 */

class NearByRestaurantsAdapterByApi(val context: Context, val item:ArrayList<NearestLocationData>?):RecyclerView.Adapter<NearByRestaurantsAdapterByApi.NearByRestViewHolder>() {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByRestViewHolder {
        return NearByRestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.near_by_rest_card,parent,false))
    }

    override fun getItemCount() = item!!.size

    override fun onBindViewHolder(holder: NearByRestViewHolder, position: Int) {
        if(item != null){
            holder.setupViews(item[position])
        }

    }

    inner class NearByRestViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var restaurantImage : ImageView = view.find(R.id.restaurant_image)
        var restaurantName : TextView =  view.find(R.id.restaurant_name)
        var restaurantDetail : TextView = view.find(R.id.restaurant_detail)
        var userNumbers : TextView = view.find(R.id.textView_user_numbers)
        var goToSwipeView : ImageView = view.find(R.id.imageView9)

        fun setupViews(model:NearestLocationData?){

            if(model != null){

                if(!model.image.isNullOrEmpty()){

//                    val photoRefrence = model.photos[0].photoReference
                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"
                    Log.e("Url","Image : $url")
                    Picasso.get().load(url).noFade().fit().centerCrop().error(R.drawable.ic_img_gallery).transform(Helpers.getPicassoTransformation(restaurantImage)).placeholder(R.drawable.ic_img_gallery).into(restaurantImage)
                }
                else {
                    Picasso.get().load(R.drawable.demo_restaurant_1).transform(Helpers.getPicassoTransformation(restaurantImage)).into(restaurantImage)
                }
                restaurantName.text = model.name
                restaurantDetail.text = model.address
                userNumbers.text = model.users.toString()
                goToSwipeView.setOnClickListener {
                    selectLocationAndGetUsersList(model.place_id, model.name)
                    context.launchActivity<SwipeMainActivity> {  }
                }
            }
        }

        fun selectLocationAndGetUsersList(location_id : String, location_name : String) {
            val location = SelectLocation(location_id, location_name)
            val call = ApiClient.getClient.selectLocation(location)

            call.enqueue(object : Callback<SelectLocationResponse> {
                override fun onFailure(call: Call<SelectLocationResponse>, t: Throwable) {
                    Log.d("Api call failure -> " , "" + call)
                }

                override fun onResponse(
                    call: Call<SelectLocationResponse>,
                    response: Response<SelectLocationResponse>
                ) {
                    var result = response.body()?.data
                    if (result != null) {

                    }
                }

            })
        }

    }
}
