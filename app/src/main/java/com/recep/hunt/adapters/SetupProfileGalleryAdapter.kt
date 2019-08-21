package com.recep.hunt.adapters

import android.content.Context
import com.recep.hunt.R
import com.recep.hunt.models.GalleryImageDetailsModel
import com.recep.hunt.models.LoginChatMessageModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*
import kotlinx.android.synthetic.main.gallery_img_adapter_item.view.*


class SetupProfileGalleryAdapter(val context: Context, private val model: GalleryImageDetailsModel) : Item<ViewHolder>(){

    override fun getLayout() = R.layout.gallery_img_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.image_name.text=model.title
        viewHolder.itemView.img_count.text= model.count.toString()



    }
}