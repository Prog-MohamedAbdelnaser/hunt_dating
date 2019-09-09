package com.recep.hunt.utilis

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.recep.hunt.R
import org.jetbrains.anko.find

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
}
