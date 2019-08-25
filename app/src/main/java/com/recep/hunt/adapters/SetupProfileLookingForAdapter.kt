package com.recep.hunt.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.recep.hunt.R
import com.recep.hunt.models.GalleryImageDetailsModel
import com.recep.hunt.models.LoginChatMessageModel
import com.recep.hunt.models.LookingForModel
import com.recep.hunt.models.LookingForSelectionModel
import com.recep.hunt.setupProfile.SetupProfileLookingForActivity
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*
import kotlinx.android.synthetic.main.gallery_img_adapter_item.view.*
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*
import org.jetbrains.anko.image


class SetupProfileLookingForAdapter(val context: Context, private var model: LookingForModel,val selectionListener:LookingForListeners) : Item<ViewHolder>(){

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
                selectionListener.getSelectedLookingFor(model.label,AddRemoveMode.Removed)
            }else{
                //select
                model.isSelected = true
                viewHolder.itemView.dates_imageView.background= context.resources.getDrawable(R.drawable.selected_cirular_btn)
                viewHolder.itemView.dates_imageView.image = context.resources.getDrawable(model.selectedImage)
                selectionListener.getSelectedLookingFor(model.label,AddRemoveMode.Added)
            }



        }

    }
}
enum class AddRemoveMode {
    Added,Removed
}
interface LookingForListeners{
    fun getSelectedLookingFor(lookingFor:String,state:AddRemoveMode?)
}