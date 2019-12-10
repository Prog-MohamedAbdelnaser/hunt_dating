package com.recep.hunt.utilis

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import org.jetbrains.anko.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mTextViewScreenTitle: TextView
    lateinit var mImageButtonBack: ImageButton
    lateinit var progressDialog: Dialog
    lateinit var fakeToolbar : RelativeLayout
    lateinit var cancelBtn : Button
    override fun setContentView(layoutResID: Int) {
        val coordinatorLayout: CoordinatorLayout = layoutInflater.inflate(R.layout.activity_base, null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.layout_container)
        mTextViewScreenTitle = coordinatorLayout.findViewById(R.id.text_screen_title) as TextView
        mImageButtonBack = coordinatorLayout.findViewById(R.id.image_back_button)
        cancelBtn = coordinatorLayout.findViewById(R.id.base_cancel_btn)
        progressDialog = Dialog(this@BaseActivity)
        layoutInflater.inflate(layoutResID, activityContainer, true)
        fakeToolbar = coordinatorLayout.findViewById(R.id.dumy_base_toolbar) as RelativeLayout

        super.setContentView(coordinatorLayout)
    }

    fun setScreenTitle(resId: Int) {
        mTextViewScreenTitle.text = getString(resId)
    }

    fun setScreenTitle(title: String) {
        mTextViewScreenTitle.text = title
    }
    fun getScreenTitle():TextView{
        return mTextViewScreenTitle
    }

    fun getBackButton(): ImageButton {
        return mImageButtonBack;
    }

    fun getToolbar():RelativeLayout{
        return fakeToolbar
    }

    fun getBaseCancelBtn():Button{
        return cancelBtn
    }

    fun showProgressDialog() {
        if(!progressDialog.isShowing){
            progressDialog.show()
        }
    }

    fun dismissProgressDialog() {
        if(progressDialog.isShowing){
            progressDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        makeUserOfline()

    }
    fun makeUserOfline()
    {
        val makeUserOnline= MakeUserOnline(false)

        val call = ApiClient.getClient.makeUserOnline(makeUserOnline, SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {

                if(response.isSuccessful){

                }

            }

        })

    }
}
