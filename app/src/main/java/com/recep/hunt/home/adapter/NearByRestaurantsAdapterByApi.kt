package com.recep.hunt.home.adapter

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.facebook.share.Share
import com.orhanobut.logger.Logger
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import org.jetbrains.anko.find
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.swipe.activities.SwipProfilesActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.*
import org.jetbrains.anko.activityManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



/**
 * Created by Rishabh Shukla
 * on 2019-08-27
 * Email : rishabh1450@gmail.com
 */

class NearByRestaurantsAdapterByApi(
    val context: Context,
    val item: ArrayList<NearestLocationData>?
) : RecyclerView.Adapter<NearByRestaurantsAdapterByApi.NearByRestViewHolder>() {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"
    private var nearbyLocation:NearestLocationData?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByRestViewHolder {
        return NearByRestViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.near_by_rest_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = item!!.size

    override fun onBindViewHolder(holder: NearByRestViewHolder, position: Int) {
        if (item != null) {
            holder.setupViews(item[position])
        }

    }

    inner class NearByRestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var restaurantImage: ImageView = view.find(R.id.restaurant_image)
        var restaurantName: TextView = view.find(R.id.restaurant_name)
        var restaurantDetail: TextView = view.find(R.id.restaurant_detail)
        var userNumbers: TextView = view.find(R.id.textView_user_numbers)
        var goToSwipeView: ImageView = view.find(R.id.imageView9)

        fun setupViews(model: NearestLocationData?) {

            if (model != null) {

//                if (!model.image.isNullOrEmpty()) {
//
//                }
                //                    val photoRefrence = model.photos[0].photoReference

                if (model.image.isNullOrEmpty().not()) {
                    val url =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"


                    //todo test converted from glide
                    Glide.with(context)
                        .load(url)
                        .error(R.drawable.ic_img_location_placeholder)
                        .placeholder(R.drawable.ic_img_location_placeholder)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                refactorImage(
                                    context.resources.getDrawable(R.drawable.ic_img_location_placeholder),
                                    restaurantImage
                                )

                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                refactorImage(resource, restaurantImage)
                                return true
                            }
                        }).submit()
                }else{
                    Glide.with(context)
                        .load(R.drawable.ic_img_location_placeholder)
                        .transform(RoundedCorners(20))
                        .into(restaurantImage)
                }


                restaurantName.text = model.name
                restaurantDetail.text = model.address
                userNumbers.text = model.users.toString()
                itemView.setOnClickListener {
                    goToSwipeView.performClick()
                }
                goToSwipeView.setOnClickListener {

                    selectLocationAndGetUsersList(model.place_id, model.name)
                }
            }
        }

        fun selectLocationAndGetUsersList( location_id: String, location_name: String) {
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

            /** for testing **/
//            val location = SelectLocation("ChIJre4ubGm1lzMRJqzbXpzzNOk"
//                , "DA'RhaYn's Lux BAR")

            val call = ApiClient.getClient.selectLocation(
                location,
                SharedPrefrenceManager.getUserToken(context)
            )

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
                        if (Utils.isSessionExpire(context, strErrorJson)) {
                            return
                        }
                    }
                    var result = response.body()?.data
                    if (result != null) {
                        getUsersList(location_id, age, date, business, friendship)
//                        getUsersList("ChIJzf5VrmoTrjsRwBKucIO-GEw", "25,30", "both", "both", "both")
//                        context.launchActivity<SwipeMainActivity> {  }
                    }
                }

            })
        }

        fun getUsersList(
            location_id: String,
            age: String,
            date: String,
            business: String,
            friendship: String
        ) {
//            val filter = UsersListFilter(location_id, age, date, business, friendship)
            // val filter = UsersListFilter("ChIJDZPv6a8lv0cRBFRz6EJVlxY01", age, date, business, friendship)
            var lat = SharedPrefrenceManager.getUserLatitude(context)
            var lang = SharedPrefrenceManager.getUserLongitude(context)
//            lat="12.8821";
//            lang="77.6722";
            val filter = UsersListFilter(location_id, age, date, business, friendship, lat, lang)
            val call =
                ApiClient.getClient.usersList(filter, SharedPrefrenceManager.getUserToken(context))

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
                        if (Utils.isSessionExpire(context, strErrorJson)) {
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
//                            try {
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
                                    result[i].totalMeeting,
                                    result[i].is_online,
                                    result[i].for_date,
                                    result[i].for_bussiness,
                                    result[i].for_friendship,
                                    images,
                                    result[i].basicInfo
                                )
                            )
//                            }catch (ex : Exception){}
                        }
                        context.launchActivity<SwipProfilesActivity> {
                            putParcelableArrayListExtra("swipeUsers", swipeUserArray)
                            Log.i("SwipeMainActivity","LOCATION_OBJECT_KEY ${item!![adapterPosition].name}")
                            putExtra(FeaturesConstants.LOCATION_OBJECT_KEY,item!![adapterPosition].name)
                        }
                        val parent = context as Activity
//                        parent.finish()
                    } else {
//                        AlertDialogUtils.displayDialog(
//                            context,
//                            context.getString(R.string.no_user_found)
//                        )
                        Thread {
                            Utils.noUserError.postValue("true")
                        }.start()
                    }
                }
            })
        }

    }

    private fun refactorImage(resource: Drawable?,imageView: ImageView) {
        var imageBitmap = resource?.toBitmap()
        imageBitmap = Helpers.createSclead(imageBitmap!!, imageView.width,imageView.height)
        val imageDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = 16.0f
        imageView.setImageDrawable(imageDrawable)


    }
}

/** TODO convert to glide
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
 */
