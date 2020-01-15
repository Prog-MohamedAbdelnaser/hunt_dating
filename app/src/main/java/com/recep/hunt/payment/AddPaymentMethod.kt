package com.recep.hunt.payment

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import kotlinx.android.synthetic.main.add_paypal_subview.view.*
import org.jetbrains.anko.find

class AddPaymentMethod : BaseActivity() {

    private lateinit var container : FrameLayout
    private lateinit var addPaypalBtn : ConstraintLayout
    private lateinit var addDebitCardBtn : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_method)
        setScreenTitle("")
        getToolbar().setBackgroundColor(Color.parseColor("#F9FCFF"))
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }
    private fun init(){
        container = find(R.id.add_payment_method_container)
        addPaypalBtn = find(R.id.add_paypal_btn)
        addDebitCardBtn = find(R.id.add_debit_card_btn)

        setupDebitCardSelection()
        addPaypalBtn.setOnClickListener { setupPaypalSelectedBtn() }
        addDebitCardBtn.setOnClickListener {  setupDebitCardSelection() }
    }
    private fun setupPaypalSelectedBtn(){
        addPaypalBtn.background = resources.getDrawable(R.drawable.selected_paypal_btn)
        addDebitCardBtn.background = resources.getDrawable(R.drawable.add_paypal_btn_bg)
        val view1 = LayoutInflater.from(applicationContext)
            .inflate(R.layout.add_paypal_subview, container, true)
    }
    private fun setupDebitCardSelection(){
        addPaypalBtn.background = resources.getDrawable(R.drawable.add_paypal_btn_bg)
        addDebitCardBtn.background = resources.getDrawable(R.drawable.selected_paypal_btn)
        val view1 = LayoutInflater.from(applicationContext)
            .inflate(R.layout.add_debit_card_subview, container, true)
    }

}
