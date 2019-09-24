package com.recep.hunt.contactUs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class FAQsActivty : BaseActivity(),ContactUsInterface {

    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs_activty)
        setScreenTitle(R.string.faqs)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()
    }
    private fun init(){
        recyclerView = find(R.id.faqs_recyclerView)
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        for(item in getData()){
            adapter.add(ContactUsAdapterItem(this,item,true,this))
        }
    }

    private fun getData():ArrayList<ContactUsModel>{
        val data = ArrayList<ContactUsModel>()
        if(data.size == 0){
            data.add(ContactUsModel(R.string.app_name,R.string.hunt_detail,R.drawable.faq_icon))
            data.add(ContactUsModel(R.string.account,R.string.account_detail,R.drawable.account_icon))
            data.add(ContactUsModel(R.string.pricing,R.string.pricing_detail,R.drawable.pricing_icon))
            data.add(ContactUsModel(R.string.card,R.string.card_detail,R.drawable.card_icon))
            data.add(ContactUsModel(R.string.payments,R.string.payments_detail,R.drawable.payments_icon))
            data.add(ContactUsModel(R.string.paypal,R.string.payments_detail,R.drawable.paypal_icon))
            data.add(ContactUsModel(R.string.mastercard,R.string.mastercard_details,R.drawable.mastercard_icon))
            data.add(ContactUsModel(R.string.google_pay,R.string.googlepay_details,R.drawable.google_icon))
            data.add(ContactUsModel(R.string.security,R.string.security_details,R.drawable.security_icon))
        }

        return data
    }
    override fun itemSelected(title: Int) {
        when(title){
            R.string.app_name->toast(R.string.app_name)
            R.string.account->toast(R.string.account)
            R.string.pricing->toast(R.string.pricing)
            R.string.card->toast(R.string.card)
            R.string.payments->toast(R.string.payments)
            R.string.paypal->toast(R.string.paypal)
            R.string.mastercard->toast(R.string.mastercard)
            R.string.google_pay->toast(R.string.google_pay)
            R.string.security->toast(R.string.security)
        }
    }

}
