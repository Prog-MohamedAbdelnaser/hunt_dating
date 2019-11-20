package com.recep.hunt.home.adapter

import android.app.Activity
import android.content.Context
import android.graphics.*
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
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import org.jetbrains.anko.activityManager
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
                    Picasso.get().load(url).error(R.drawable.ic_img_gallery).transform(RoundedTransformation(20, 0)).placeholder(R.drawable.ic_img_gallery).into(restaurantImage)
                }
                restaurantName.text = model.name
                restaurantDetail.text = model.address
                userNumbers.text = (model.users - 1).toString()
                goToSwipeView.setOnClickListener {
                    selectLocationAndGetUsersList(model.place_id, model.name)
                }
            }
        }

        fun selectLocationAndGetUsersList(location_id : String, location_name : String) {
            var leftAge = SharedPrefrenceManager.getUserInterestedAgeFrom(context)
            if (leftAge.isEmpty())
                leftAge = "18"
            var rightAge = SharedPrefrenceManager.getUserInterestedAgeTo(context)
            if (rightAge.isEmpty())
                rightAge = "50"
            val age = leftAge + "," + rightAge
            var date = SharedPrefrenceManager.getUserLookingFor(context, "Date")
            var business = SharedPrefrenceManager.getUserLookingFor(context, "Business")
            var friendship = SharedPrefrenceManager.getUserLookingFor(context, "Friendship")
            date = date.toLowerCase()
            business = business.toLowerCase()
            friendship = friendship.toLowerCase()
            val location = SelectLocation(location_id, location_name)
            val call = ApiClient.getClient.selectLocation(location, SharedPrefrenceManager.getUserToken(context))

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
                        getUsersList(location_id, age, date, business, friendship)
//                        context.launchActivity<SwipeMainActivity> {  }
                    }
                }

            })
        }

        fun getUsersList(location_id: String, age: String, date : String, business : String, friendship : String) {
            val filter = UsersListFilter(location_id, age, date, business, friendship)
//            val filter = UsersListFilter("ChIJDZPv6a8lv0cRBFRz6EJVlxY01", age, date, business, friendship)
            val call = ApiClient.getClient.usersList(filter, SharedPrefrenceManager.getUserToken(context))

            call.enqueue(object : Callback<UsersListResponse> {
                override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                    Log.d("Api call failure -> " , "" + call)
                }

                override fun onResponse(
                    call: Call<UsersListResponse>,
                    response: Response<UsersListResponse>
                ) {
                    var result = response.body()?.data
                    var swipeUserArray = ArrayList<SwipeUserModel>()
                    if (result != null && result.size > 0) {
                        for (i in 0 until result.size) {
                            val images = ArrayList<String>()
                            if ( result[i].user_profile_image.size != 0) {
                                for (j in 0 until result[i].user_profile_image.size) {
                                    images.add(result[i].user_profile_image[j].image)
                                }
                            }
                            else {
                                val baseUrl = "https://hunt.nyc3.digitaloceanspaces.com/User/"
                                images.add(baseUrl + result[i].profile_pic)
                            }
                            swipeUserArray.add(SwipeUserModel(result[i].id, result[i].location_name, result[i].first_name, result[i].age, result[i].basicInfo.job_title, result[i].basicInfo.about, images))
                        }
                        context.launchActivity<SwipeMainActivity> { putParcelableArrayListExtra("swipeUsers", swipeUserArray) }
                        val parent = context as Activity
                        parent.finish()
                    }
                }
            })
        }

    }
}

class RoundedTransformation(val radius : Int, val margin : Int) : com.squareup.picasso.Transformation {
    override fun key(): String {
        return "rounded(r=" + radius + ", m=" + margin + ")"
    }

    override fun transform(source: Bitmap?): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        val output = Bitmap.createBitmap(source!!.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (source.width - margin).toFloat(), (source.height - margin).toFloat()), radius.toFloat(), radius.toFloat(), paint)
        if (source != output) {
            source.recycle()
        }
        return output
    }

}
