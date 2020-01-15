package com.recep.hunt.premium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import android.text.TextUtils
import android.text.Editable
import android.text.TextWatcher
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.ExpiryDateFormatWatcher
import com.recep.hunt.utilis.FourDigitCardFormatWatcher


class AddNewPaymentActivity : BaseActivity() {

    private lateinit var showMasterLayoutBtn : ImageButton
    private var isMasterListVisible = false
    private lateinit var masterCardLayout : LinearLayout
    private lateinit var masterCardNumberEditText: EditText
    private lateinit var masterCardExpirydateEditText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_payment)
        setScreenTitle(R.string.payment)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()

    }
    private fun init(){
        showMasterLayoutBtn = find(R.id.master_card_show_btn)
        masterCardLayout = find(R.id.master_card_layout)
        masterCardNumberEditText = find(R.id.master_card_number_et)
        masterCardExpirydateEditText = find(R.id.master_card_expiry_date_et)

        showMasterLayout()
        showMasterLayoutBtn.setOnClickListener {
            if(isMasterListVisible){
                isMasterListVisible = false
                showMasterLayout()
            }else{
                isMasterListVisible = true
                hideMasterLayout()
            }
        }

        masterCardNumberEditText.addTextChangedListener(FourDigitCardFormatWatcher())
        masterCardExpirydateEditText.addTextChangedListener(ExpiryDateFormatWatcher())


    }
    private fun showMasterLayout(){
        showMasterLayoutBtn.image = resources.getDrawable(R.drawable.ic_remove)
        masterCardLayout.visibility = View.VISIBLE
    }
    private fun hideMasterLayout(){
        showMasterLayoutBtn.image = resources.getDrawable(R.drawable.ic_add_black)
        masterCardLayout.visibility = View.GONE
    }
}

