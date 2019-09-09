package com.recep.hunt.setupProfile.adapters

import android.content.Context
import com.recep.hunt.R
import com.recep.hunt.setupProfile.model.LookingForModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor


class SetupProfileLookingForAdapter(val context: Context, private var model: LookingForModel, val selectionListener: LookingForListeners) : Item<ViewHolder>(){

//    private var lookingForModel = ArrayList<LookingForSelectionModel>()
    override fun getLayout() = R.layout.looking_for_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title_tv.text=model.label
        viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.unselected_circular_btn)
        viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.unSelectedImage)

        viewHolder.itemView.dates_imageView.setOnClickListener {
            if(model.isSelected){
                //unselect
                model.isSelected = false
                viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.unselected_circular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.unSelectedImage)
                selectionListener.getSelectedLookingFor(model.label,
                    AddRemoveMode.Removed
                )
                viewHolder.itemView.title_tv.textColor = context.resources.getColor(R.color.app_light_text_color)
            }else{
                //select
                model.isSelected = true
                viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.selected_cirular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.selectedImage)
                selectionListener.getSelectedLookingFor(model.label,
                    AddRemoveMode.Added
                )
                viewHolder.itemView.title_tv.textColor = context.resources.getColor(R.color.app_text_black)
            }



        }

    }
}
enum class AddRemoveMode {
    Added,Removed
}
interface LookingForListeners{
    fun getSelectedLookingFor(lookingFor:String,state: AddRemoveMode?)
}