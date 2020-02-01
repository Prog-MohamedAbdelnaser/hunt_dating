package com.recep.hunt.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.Helpers.Companion.createSclead
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.*
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.imageView9
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_item_detail
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_item_name
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_list_image
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.textView_user_numbers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt





/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

class NearByRestaurantsVerticalAdapterByAPi(
    val context: Context,
    val item: ArrayList<NearestLocationData>?
) : Item<ViewHolder>() {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"

    override fun getLayout() = R.layout.vertical_restaurant_list_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.imageView9.visibility = View.VISIBLE
        if (item != null) {
            try {
                val model = item[position - 1]
                if (!model.image.isNullOrEmpty()) {
                    val url =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"

                    //TODO test converted to glide
                    Glide
                        .with(context)
                        .load(url)
                        .transform(RoundedCorners(20))
                        .error(R.drawable.ic_img_location_placeholder)
                        .placeholder(R.drawable.ic_img_location_placeholder)
                        .into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                else {
                    Glide.with(context)
                        .load(R.drawable.ic_img_location_placeholder)
                        .transform(RoundedCorners(20))
                        .into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                viewHolder.itemView.restaurant_vertical_item_name.text = model.name
                viewHolder.itemView.restaurant_vertical_item_detail.text = model.address
                viewHolder.itemView.textView_user_numbers.text = model.users.toString()
                viewHolder.itemView.textView_distance_numbers.text = "${model.distance.roundToInt()} M"
                viewHolder.itemView.imageView9.setOnClickListener {
                    selectLocationAndGetUsersList(model.place_id, model.name)
                }
                viewHolder.itemView.setOnClickListener {
                    viewHolder.itemView.imageView9.performClick()
                }


            } catch (e: Exception) {
                Log.e("Execpetion", "$e")
            }


        }

    }

    private fun refactorImage(resource: Drawable?,imageView: ImageView) {
        var imageBitmap = resource?.toBitmap()
        imageBitmap =createSclead(imageBitmap!!,500,400)
        val imageDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = 16.0f
       imageView.setImageDrawable(imageDrawable)

    }

    fun selectLocationAndGetUsersList(location_id: String, location_name: String) {
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
                var result = response.body()?.data
                if (result != null) {

                    getUsersList(location_id, age, date, business, friendship,location_name)
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
        friendship: String,
        locationName: String
    ) {
        val lat = SharedPrefrenceManager.getUserLatitude(context)
        val lang = SharedPrefrenceManager.getUserLongitude(context)
        val filter = UsersListFilter(location_id, age, date, business, friendship, lat, lang)
//            val filter = UsersListFilter("ChIJDZPv6a8lv0cRBFRz6EJVlxY01", age, date, business, friendship)
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
                    context.launchActivity<SwipeMainActivity> {
                        putParcelableArrayListExtra("swipeUsers", swipeUserArray)
                        putExtra(FeaturesConstants.LOCATION_OBJECT_KEY, locationName)

                    }
                } else {
                    Thread {
                        Utils.noUserError.postValue("true")
                    }.start()
//                    AlertDialogUtils.displayDialog(
//                        context,
//                        context.getString(R.string.no_user_found)
//                    )
                }
            }
        })
    }


}
