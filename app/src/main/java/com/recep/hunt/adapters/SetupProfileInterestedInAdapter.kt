package com.recep.hunt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.models.LookingForModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.image


class SetupProfileInterestedInAdapter( private var model: ArrayList<LookingForModel>, val context: Context,private val lookingForListeners: LookingForListeners) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.looking_for_adapter_item, parent, false))

    }

    override fun getItemCount(): Int {
       return model.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title_tv.text = model[position].label
        viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model[position].unSelectedImage)

        if(!model[position].isSelected) {
            viewHolder.itemView.dates_imageView.background =
                context.resources.getDrawable(R.drawable.unselected_circular_btn)
            viewHolder.itemView.dates_imageView.image =
                context.resources.getDrawable(model[position].unSelectedImage)
        }
        else {
            viewHolder.itemView.dates_imageView.background =
                context.resources.getDrawable(R.drawable.selected_cirular_btn)
            viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model[position].selectedImage)
        }


        viewHolder.itemView.dates_imageView.setOnClickListener {

            if (model[position].isSelected) {
                reset()
                //unselect
                model[position].isSelected = false

                viewHolder.itemView.dates_imageView.background =
                    context.resources.getDrawable(R.drawable.unselected_circular_btn)
                viewHolder.itemView.dates_imageView.image =
                    context.resources.getDrawable(model[position].unSelectedImage)

            } else {
                reset()
                model[position].isSelected = true

                viewHolder.itemView.dates_imageView.background = context.resources.getDrawable(R.drawable.selected_cirular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model[position].selectedImage)

                lookingForListeners.getSelectedLookingFor(model[position].value.toString(),null)
            }
            notifyDataSetChanged()

        }

    }
    private fun reset() {
        for (data in model) {
           data.isSelected=false
        }
    }


}
