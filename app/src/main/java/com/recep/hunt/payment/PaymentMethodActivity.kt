package com.recep.hunt.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.activity_select_payment_methods.*
import org.jetbrains.anko.find

class PaymentMethodActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noPaymentMethodsImageView : ImageView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        setScreenTitle("")
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()

    }
    private fun init(){
        recyclerView = find(R.id.payment_method_recyclerView)
        noPaymentMethodsImageView = find(R.id.no_payment_methods_imageView)
        add_payment_method_btn.setOnClickListener {
            launchActivity<TrasactionDone>()
        }

        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDebitCardItem()
        addPaypalItem()

    }
    private fun addDebitCardItem(){
        noPaymentMethodsImageView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.add(DebitCardItem())
    }
    private fun addPaypalItem(){
        noPaymentMethodsImageView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.add(PaypalItem())
    }

}
class DebitCardItem():Item<ViewHolder>(){
    override fun getLayout() = R.layout.new_debit_card_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}
class PaypalItem():Item<ViewHolder>(){
    override fun getLayout() = R.layout.new_paypal_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}