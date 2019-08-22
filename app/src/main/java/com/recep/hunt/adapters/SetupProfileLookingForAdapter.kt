package com.recep.hunt.adapters

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.recep.hunt.R
import com.recep.hunt.models.GalleryImageDetailsModel
import com.recep.hunt.models.LoginChatMessageModel
import com.recep.hunt.models.LookingForModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*
import kotlinx.android.synthetic.main.gallery_img_adapter_item.view.*
import kotlinx.android.synthetic.main.looking_for_adapter_item.view.*


class SetupProfileLookingForAdapter(val context: Context, private val model: ArrayList<LookingForModel>) : Item<ViewHolder>(){

    override fun getLayout() = R.layout.looking_for_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title_tv.text=model.get(position).label
      //  viewHolder.itemView.title_tv.text=model.get(position).label
        viewHolder.itemView.dates_imageView.background= Resources.getSystem().getDrawable(model.get(position).image)

        viewHolder.itemView.dates_imageView.setOnClickListener(View.OnClickListener {

        })

    }
}