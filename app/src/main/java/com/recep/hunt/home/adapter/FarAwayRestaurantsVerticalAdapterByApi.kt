package com.recep.hunt.home.adapter

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.model.nearestLocation.NearestLocationData
import com.recep.hunt.utilis.Helpers
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.vertical_far_restaurant_list_item_layout.view.*
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.*
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_item_detail
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_item_name
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.restaurant_vertical_list_image
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.textView_user_numbers
import org.jetbrains.anko.find
import java.lang.Exception


/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

class FarAwayRestaurantsVerticalAdapterByApi(val context: Context, val item:ArrayList<NearestLocationData>?): Item<ViewHolder>() {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"

    override fun getLayout() = R.layout.vertical_far_restaurant_list_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if(item != null){
            try{
                val model = item[position]
                if(!model.image.isNullOrEmpty()){
                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"
//                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.photos[0].photoReference}&key=${context.resources.getString(R.string.google_api_key)}"
                    Picasso.get().load(url).error(R.drawable.ic_img_gallery).transform(Helpers.getPicassoTransformation(viewHolder.itemView.restaurant_vertical_list_image)).placeholder(R.drawable.ic_img_gallery).into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                else {
                    Picasso.get().load(R.drawable.demo_restaurant_1).transform(Helpers.getPicassoTransformation(viewHolder.itemView.restaurant_vertical_list_image)).into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                viewHolder.itemView.restaurant_vertical_item_name.text = model.name
                viewHolder.itemView.restaurant_vertical_item_detail.text = model.address
                viewHolder.itemView.textView_user_numbers.text = model.users.toString()
                val distance = model.distance.toFloat() * 1000
                viewHolder.itemView.textView_distance_numbers.text = distance.toString() + " M"

            }catch (e:Exception){
                Log.e("Execpetion","$e")
            }



        }


    }
}