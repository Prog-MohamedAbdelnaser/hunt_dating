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
import org.aviran.cookiebar2.CookieBar


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

        fun showInternetSnackBar(activity: Activity){
            CookieBar.build(activity)
                .setTitle("\t\tNo Internet")
                .setMessage("check your Internet Connection")
                .setIcon(R.drawable.ic_signal_wifi_off_black_24dp)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.colorAccent)
                .setTitleColor(R.color.white)
                .setMessageColor(R.color.white)
                .setSwipeToDismiss(true)
                .show()
        }
        fun showImageSnackBar(activity: Activity,color:Int,image:Int,title:String,detail:String,titleColor: Int,detailColor:Int,cokkiePosition:Int){
            CookieBar.build(activity)
                .setTitle("\t\t$title")
                .setMessage(detail)
                .setIcon(image)
                .setCookiePosition(cokkiePosition)
                .setBackgroundColor(color)
                .setTitleColor(titleColor)
                .setMessageColor(detailColor)
                .setSwipeToDismiss(true)
                .show()
        }
        fun showSuccesSnackBar(activity: Activity,title: String,msg:String){
            CookieBar.build(activity)
                .setTitle("\t$title")
                .setMessage(msg)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.materialgreen)
                .setTitleColor(R.color.white)
                .setMessageColor(R.color.white)
                .setSwipeToDismiss(true)
                .show()
        }
        fun showErrorSnackBar(activity: Activity,title: String,msg:String){
            CookieBar.build(activity)
                .setTitle("\t$title")
                .setMessage(msg)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.materialred)
                .setTitleColor(R.color.white)
                .setMessageColor(R.color.white)
                .setIcon(R.drawable.ic_error_white)
                .setSwipeToDismiss(true)
                .show()
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