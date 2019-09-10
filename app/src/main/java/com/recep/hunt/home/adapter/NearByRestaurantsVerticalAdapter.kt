package com.recep.hunt.home.adapter

import android.content.Context
import android.util.Log
import com.recep.hunt.R
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.utilis.Helpers
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.vertical_restaurant_list_item_layout.view.*
import java.lang.Exception


/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

class NearByRestaurantsVerticalAdapter(val context: Context, val item:ArrayList<NearByRestaurantsModelResults>?): Item<ViewHolder>() {
    override fun getLayout() = R.layout.vertical_restaurant_list_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if(item != null){
            try{
                val model = item[position]
                if(model.photos != null){
                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${model.photos[0].photoReference}&key=${context.resources.getString(R.string.google_api_key)}"
                    Picasso.get().load(url).error(R.drawable.ic_img_gallery).transform(Helpers.getPicassoTransformation(viewHolder.itemView.restaurant_vertical_list_image)).placeholder(R.drawable.ic_img_gallery).into(viewHolder.itemView.restaurant_vertical_list_image)
                }
                viewHolder.itemView.restaurant_vertical_item_name.text = model.name
                viewHolder.itemView.restaurant_vertical_item_detail.text = model.vicinity

            }catch (e:Exception){
                Log.e("Execpetion","$e")
            }



        }


    }
}