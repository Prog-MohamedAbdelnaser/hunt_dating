package com.recep.hunt.creator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.creator.model.CreatorQustion
import com.recep.hunt.creator.model.OptionModel
import com.recep.hunt.home.adapter.NearByRestaurantsAdapter
import com.recep.hunt.home.model.nearByRestaurantsModel.NearByRestaurantsModelResults
import com.recep.hunt.payment.model.FaqPayments
import com.recep.hunt.utilis.Helpers
import org.jetbrains.anko.find

class CreatorOptionAdapter(val context: Context,val item:ArrayList<OptionModel>?):RecyclerView.Adapter<CreatorOptionAdapter.CreatorOptionViewholder>() {

    var onItemClick: ((OptionModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorOptionViewholder {
        return CreatorOptionViewholder(LayoutInflater.from(parent.context).inflate(R.layout.creator_options_item,parent,false))
    }

    override fun getItemCount() = item!!.size

    override fun onBindViewHolder(holder: CreatorOptionViewholder, position: Int) {

            holder.setupViews(item?.get(position))



    }

    inner class CreatorOptionViewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var option: Button = itemView.find(R.id.optionButton)

        init {
            itemView.setOnClickListener {
                item?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
            }
        }
        fun setupViews(model:OptionModel?){

            if(model != null){
                option.text=model.optionText

            }



        }
    }
}