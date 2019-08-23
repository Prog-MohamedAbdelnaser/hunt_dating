package com.recep.hunt.utilis

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.RecyclerView
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.constants.Constants


/**
 * Created by Rishabh Shukla
 * on 2019-08-18
 * Email : rishabh1450@gmail.com
 */

class Helpers {

    companion object{

        fun showDialog(activity: Activity, context: Context, title:String): KProgressHUD {
            return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setCancellable(false)
                .setBackgroundColor(context.resources.getColor(R.color.transparent_black))
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        fun isInternetConnection(context: Context): Boolean {
            val cn =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nf = cn.activeNetworkInfo
            val statusInternet: Boolean

            statusInternet = if (nf != null && nf.isConnected) {
                Log.i("Info:", "Network Available.")
                true
            } else {
                Log.i("Info:", "Network Not Available.")
                false

            }
            return statusInternet
        }

        fun runAnimation(recyclerView: RecyclerView) {
            val context = recyclerView.context
            val controller: LayoutAnimationController
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom)
            recyclerView.layoutAnimation = controller
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }

}