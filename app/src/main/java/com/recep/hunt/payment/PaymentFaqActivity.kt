package com.recep.hunt.payment

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.payment.adapter.FaqPaymentAdapter
import com.recep.hunt.payment.model.FaqPayments
import com.recep.hunt.utilis.BaseActivity
import org.jetbrains.anko.find
import androidx.recyclerview.widget.PagerSnapHelper
import me.relex.circleindicator.CircleIndicator2


class PaymentFaqActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var indicator : CircleIndicator2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_faq)
        val s = Html.fromHtml(resources.getString(R.string.payment_faq)).toString()
        setScreenTitle(s)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()
    }

    private fun init() {
        recyclerView = find(R.id.rvFaqsList)
        indicator = find(R.id.indicator)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = FaqPaymentAdapter(this, getFaqs())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);



        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)



        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper)



    }

    private fun getFaqs(): ArrayList<FaqPayments> {
        var faqs = ArrayList<FaqPayments>()

        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?Explain all",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list,"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money or by adding a new recipient and typing in their phone number"
            )
        )
        faqs.add(
            FaqPayments(
                0,
                "What is Request Payments?",
                "Request payments allows you to easily  request money from someone by simply selecting them from your phone's contacts list, or by adding a new recipient and typing in their phone number, or by adding a new recipient and typing in their phone number  or by adding a new recipient and typing in their phone number"
            )
        )

        return faqs


    }
}
