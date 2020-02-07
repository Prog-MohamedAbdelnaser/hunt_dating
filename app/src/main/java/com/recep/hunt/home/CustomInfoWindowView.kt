package com.recep.hunt.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.orhanobut.logger.Logger
import com.recep.hunt.R
import com.recep.hunt.model.nearestLocation.NearestLocationData
import kotlinx.android.synthetic.main.custom_infowindow.view.*
import kotlin.math.roundToInt

class CustomInfoWindowView(val context: Context) : GoogleMap.InfoWindowAdapter {

    private var GOOGLE_API_KEY_FOR_IMAGE = "AIzaSyD_MwCA8Z2IKyoyV0BEsAxjZZrkokUX_jo"

    override fun getInfoWindow(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_infowindow, null)
        view.alpha = 1.0f
        marker?.let {
            view.info_window_rest_name.text = marker.title
            it.setInfoWindowAnchor(7f,0f)
//            if (it.tag != null) {
                val locationInfo = it.tag as NearestLocationData

                if(locationInfo.distance.roundToInt() > HomeActivity.NEAREST_DISTANCE ){
                    view.textView30.setTextColor(ActivityCompat.getColor(context, R.color.font_color_far_location))
                    view.textView31.setTextColor(ActivityCompat.getColor(context, R.color.font_color_far_location))
                    view.image_view_location_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_far_away_distance))
                    view.image_view_user_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_far_away_rest_user))
                }
                else{
                    view.textView30.setTextColor(ActivityCompat.getColor(context, R.color.font_color_near_location))
                    view.textView31.setTextColor(ActivityCompat.getColor(context, R.color.font_color_near_location))
                    view.image_view_location_icon.setColorFilter(ActivityCompat.getColor(context, R.color.font_color_near_location), android.graphics.PorterDuff.Mode.MULTIPLY)
                    view.image_view_user_icon.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_near_by_rest_user))
                }

                view.textView30.text = (locationInfo.users).toString()

                if (locationInfo.distance < 1000) {
                    view.textView31.setText("${locationInfo.distance.roundToInt()} M")
                }
                else{
                    view.textView31.setText("${String.format("%.2f", locationInfo.distance/1000)} KM")
                }

                val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${locationInfo.image}&key=${GOOGLE_API_KEY_FOR_IMAGE}"
                Logger.d("url = $url")
                Glide.with(context)
                    .load(url)
                    .transform(RoundedCorners(20))
                    .placeholder(R.drawable.ic_img_location_placeholder)
                    .into(view.info_window_rest_image)

//            } else {
//                view.textView30.text = 0.toString()
//                view.textView31.text = "0 M"
//            }
        }
        return view
    }

    //getInfoWindow
    override fun getInfoContents(p0: Marker?) = null

}