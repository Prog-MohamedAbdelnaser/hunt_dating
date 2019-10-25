package com.recep.hunt.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_select_payment_methods.*
import org.jetbrains.anko.find

class SelectPaymentMethodsActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment_methods)
        setScreenTitle("")
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }
    private fun init(){
        this.hideKeyboard()
        recyclerView = find(R.id.select_methods_recyclerView)
        setupRecyclerView()

        select_methods_add_btn.setOnClickListener {
            launchActivity<AddPaymentMethod>()
        }
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        addDebitCardItem()
        addPaypalItem()
    }
    private fun addDebitCardItem(){
        adapter.add(HorizontalDebitCardItem())
    }
    private fun addPaypalItem(){
        adapter.add(HorizontalPaypalCardItem())
    }
}
class HorizontalDebitCardItem():Item<ViewHolder>(){
    override fun getLayout() = R.layout.new_horizontal_debit_card_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class HorizontalPaypalCardItem():Item<ViewHolder>(){
    override fun getLayout() = R.layout.new_horizontal_paypal_card_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

