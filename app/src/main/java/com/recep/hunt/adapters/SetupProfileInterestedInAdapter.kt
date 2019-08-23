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

class SetupProfileInterestedInAdapter( private var model: ArrayList<LookingForModel>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.looking_for_adapter_item, parent, false))

    }

    override fun getItemCount(): Int {
       return model.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title_tv.text = model.get(position).label
//        viewHolder.itemView.dates_imageView.background= Resources.getSystem().getDrawable(model.unSelectedImage)
        if(  model.get(position).isSelected == false) {
            viewHolder.itemView.dates_imageView.background =
                context.resources.getDrawable(R.drawable.unselected_circular_btn)
        }
        else
        {
            viewHolder.itemView.dates_imageView.background =
                context.resources.getDrawable(R.drawable.selected_cirular_btn)
        }
        viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.get(position).unSelectedImage)
        viewHolder.itemView.dates_imageView.setOnClickListener {

            if (model.get(position).isSelected) {
                reset()
                //unselect
                model.get(position).isSelected = false

                viewHolder.itemView.dates_imageView.background =
                    context.resources.getDrawable(R.drawable.unselected_circular_btn)
                viewHolder.itemView.dates_imageView.image =
                    context.resources.getDrawable(model.get(position).unSelectedImage)

            } else {
                reset()
                model.get(position).isSelected = true

                viewHolder.itemView.dates_imageView.background =
                    context.resources.getDrawable(R.drawable.selected_cirular_btn)
                viewHolder.itemView.dates_imageView.image =
                    context.resources.getDrawable(model.get(position).selectedImage)
            }
notifyDataSetChanged()

        }

    }



fun reset() {
    for (data in model) {
       data.isSelected=false
    }
}


}
