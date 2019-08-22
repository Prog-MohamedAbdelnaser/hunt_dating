package com.recep.hunt.adapters

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.models.GalleryImageDetailsModel
import com.recep.hunt.models.LoginChatMessageModel
import com.recep.hunt.models.LookingForModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*
import kotlinx.android.synthetic.main.gallery_img_adapter_item.view.*
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.image


class SetupProfileLookingForAdapter(val context: Context, private var model: LookingForModel) : Item<ViewHolder>(){

    override fun getLayout() = R.layout.looking_for_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title_tv.text=model.label
//        viewHolder.itemView.dates_imageView.background= Resources.getSystem().getDrawable(model.unSelectedImage)
        viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.unselected_circular_btn)
        viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.unSelectedImage)

        viewHolder.itemView.dates_imageView.setOnClickListener {
            if(model.isSelected){
                //unselect
                model.isSelected = false
                viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.unselected_circular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.unSelectedImage)

            }else{
                //select
                model.isSelected = true
                viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.selected_cirular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.selectedImage)
            }
        }

    }
}
