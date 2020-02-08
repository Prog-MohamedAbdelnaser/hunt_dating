package com.recep.hunt.home.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.base.adapter.BaseAdapter
import com.recep.hunt.base.adapter.BaseViewHolder
import com.recep.hunt.model.SelectLocation
import com.recep.hunt.model.UsersListFilter
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.swipe.activities.SwipProfilesActivity
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import com.rent.client.base.widget.loadImageFromUrl
import kotlinx.android.synthetic.main.faraway_restaurant_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class NearRestaurantHorizontalAdapter:BaseAdapter<NearestLocationData>(itemLayoutRes = R.layout.faraway_restaurant_item) {

   private var nearbySize:Int=0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<NearestLocationData> {
            return NearRestaurantBaseViewHolder(getItemView(parent))
    }

    fun setNearbySize(int: Int){
        nearbySize=int
    }


    inner class NearRestaurantBaseViewHolder(view: View) : BaseViewHolder<NearestLocationData>(view){
        override fun fillData() {
            itemView.apply {

                if (adapterPosition<=nearbySize-1) {
                    ivForward.visibility=View.VISIBLE
                    ivUsers.setImageResource(R.drawable.ic_near_by_rest_user)
                    tvUserNumbers.setTextColor(context.resources.getColor(R.color.colorAccent))
                }else{
                    ivForward.visibility=View.GONE
                    ivUsers.setImageResource(R.drawable.ic_far_away_rest_user)
                    tvUserNumbers.setTextColor(context.resources.getColor(R.color.farAwayHeighlightColor))
                }
                tvRestaurantName.text=item?.name
                tvRestaurantDetail.text=item?.address
                tvUserNumbers.text=item?.users.toString()

                item?.image?.let { ivRestaurantImage.loadImageFromUrl(it,errorPlaceHolder = R.drawable.ic_img_location_placeholder,
                    loadingPlaceHolder = R.drawable.ic_img_location_placeholder) }

                ivForward.setOnClickListener { item?.place_id?.let { it1 -> selectLocationAndGetUsersList(it1, item?.name?:"",context) } }
            }
        }
    }


    fun selectLocationAndGetUsersList( location_id: String, location_name: String,context:Context) {
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
                    getUsersList(location_id, age, date, business, friendship,context)
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
        friendship: String,
        context: Context
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
                                result[i].totalMeeting.roundToInt(),
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
                        putExtra(FeaturesConstants.LOCATION_OBJECT_KEY,getItems()[getCurrentPosition()].name)
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