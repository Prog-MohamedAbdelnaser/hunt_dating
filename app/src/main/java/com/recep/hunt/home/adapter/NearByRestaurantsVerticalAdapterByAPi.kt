package com.recep.hunt.home.adapter

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.swipe.SwipeMainActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.AlertDialogUtils
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        if (item != null) {
            try {
                val model = item[position - 1]
                if (!model.image.isNullOrEmpty()) {
                    val url =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"
//                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.photos[0].photoReference}&key=${context.resources.getString(R.string.google_api_key)}"

                    //TODO test converted to glide
                    Glide.with(context)
                        .load(url)
                        .error(R.drawable.ic_img_gallery)
//                        .transform(RoundedTransformation(20, 0))
//                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_img_gallery)
                        .into(viewHolder.itemView.restaurant_vertical_list_image)
                } else {
                    //todo test converted to glide
                    Glide.with(context)
                        .load(R.drawable.demo_restaurant_1)
                        //.transform(Helpers.getPicassoTransformation(viewHolder.itemView.restaurant_vertical_list_image))
                        .into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                viewHolder.itemView.restaurant_vertical_item_name.text = model.name
                viewHolder.itemView.restaurant_vertical_item_detail.text = model.address
                viewHolder.itemView.textView_user_numbers.text = model.users.toString()

                viewHolder.itemView.imageView9.setOnClickListener {
                    selectLocationAndGetUsersList(model.place_id, model.name)
                }


            } catch (e: Exception) {
                Log.e("Execpetion", "$e")
            }


        }

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
                    getUsersList(location_id, age, date, business, friendship)
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
                                result[i].first_name,
                                result[i].age,
                                result[i].basicInfo.job_title,
                                result[i].basicInfo.about,
                                result[i].totalMatching,
                                result[i].totalMeeting,
                                result[i].is_online,
                                result[i].for_date,
                                result[i].for_business,
                                result[i].for_friendship,
                                images,
                                result[i].basicInfo
                            )
                        )
                    }
                    context.launchActivity<SwipeMainActivity> {
                        putParcelableArrayListExtra(
                            "swipeUsers",
                            swipeUserArray
                        )
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