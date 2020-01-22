package com.recep.hunt.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.utilis.Helpers


abstract class BaseActivity : AppCompatActivity() {

    lateinit var progressDialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog= Helpers.createProgressDialog(this,getString(R.string.loading))
    }
    
    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    fun showProgressDialog(){
        progressDialog.show()
    }

    fun hideProgressDialog(){
        progressDialog.dismiss()
    }
}