package com.recep.hunt.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.recep.hunt.R
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.utilis.Helpers
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick


/**
 * Created by Rishabh Shukla
 * on 2019-08-27
 * Email : rishabh1450@gmail.com
 */

class NearByRestaurantsAdapter(val context: Context,
                               val item:ArrayList<NearByRestaurantsModelResults>?,
                               val nearByRestaurantsAdapterListener : NearByRestaurantsAdapterListener)
    :RecyclerView.Adapter<NearByRestaurantsAdapter.NearByRestViewHolder>() {

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

        init {
            view.onClick {
                nearByRestaurantsAdapterListener.onNearByRestaurantsClicked(adapterPosition)
            }
        }

        fun setupViews(model:NearByRestaurantsModelResults?){

            if(model != null){

                if(model.photos != null){
//                    val photoRefrence = model.photos[0].photoReference
//                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoRefrence&key=${context.resources.getString(R.string.google_api_key)}"
//                    Log.e("Url","Image : $url")
//                    Picasso.get().load(url).noFade().fit().centerCrop().error(R.drawable.ic_img_gallery).transform(Helpers.getPicassoTransformation(restaurantImage)).placeholder(R.drawable.ic_img_gallery).into(restaurantImage)
                }

                //
                Glide.with(context)
                    .load(R.drawable.demo_restaurant_1)
                    //.transform(Helpers.getPicassoTransformation(restaurantImage))
                    .apply(RequestOptions.circleCropTransform())
                    .into(restaurantImage)
                restaurantName.text = model.name
                restaurantDetail.text = model.vicinity
            }



        }
    }

    interface NearByRestaurantsAdapterListener{
        fun onNearByRestaurantsClicked(position: Int)
    }


}
