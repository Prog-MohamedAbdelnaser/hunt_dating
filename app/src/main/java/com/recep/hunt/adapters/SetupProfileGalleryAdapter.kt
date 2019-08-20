package com.recep.hunt.adapters

import android.content.Context
import com.recep.hunt.R
import com.recep.hunt.models.LoginChatMessageModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*


/**
 * Created by Rishabh Shukla
 * on 2019-08-19
 * Email : rishabh1450@gmail.com
 */

class SetupProfileGalleryAdapter(val context: Context, private val model: ArrayList<String>) : Item<ViewHolder>(){

    override fun getLayout() = R.layout.chat_message_sent_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}