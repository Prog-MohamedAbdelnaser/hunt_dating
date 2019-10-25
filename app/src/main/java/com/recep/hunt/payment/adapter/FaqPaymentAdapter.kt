package com.recep.hunt.payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.home.adapter.NearByRestaurantsAdapter
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.payment.model.FaqPayments
import com.recep.hunt.utilis.Helpers
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find

class FaqPaymentAdapter(val context: Context,val item:ArrayList<FaqPayments>?):RecyclerView.Adapter<FaqPaymentAdapter.FaqPaymentViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqPaymentViewholder {
        return FaqPaymentViewholder(LayoutInflater.from(parent.context).inflate(R.layout.payment_faq_item,parent,false))
    }

    override fun getItemCount() = item!!.size

    override fun onBindViewHolder(holder: FaqPaymentViewholder, position: Int) {
        if(item != null){
            holder.setupViews(item[position])
        }

    }

    inner class FaqPaymentViewholder(view: View) : RecyclerView.ViewHolder(view){
        var question : TextView = view.find(R.id.tvQuestion)
        var answer : TextView =  view.find(R.id.tvAnswer)


        fun setupViews(model:FaqPayments?){

            if(model != null){
                question.text=model.question
                answer.text=model.answer
            }



        }
    }
}


