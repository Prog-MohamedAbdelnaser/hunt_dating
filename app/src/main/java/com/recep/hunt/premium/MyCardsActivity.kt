package com.recep.hunt.premium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_my_cards.*

class MyCardsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cards)
        setScreenTitle("My Cards")
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()

    }
    private fun init(){
        my_card_recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = GroupAdapter<ViewHolder>()
        my_card_recyclerView.adapter = adapter
        adapter.add(MasterCardItemAdapter(this))
        adapter.add(GooglePayCardItemAdapter(this))
        adapter.add(PaypalCardItemAdapter(this))
        adapter.add(AddNewPaymentMethodAdapter(this))


    }
}
