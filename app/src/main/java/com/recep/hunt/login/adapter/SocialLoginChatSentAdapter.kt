package com.recep.hunt.login.adapter

import android.content.Context
import com.recep.hunt.R
import com.recep.hunt.login.model.LoginChatMessageModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.chat_message_sent_adapter_item.view.*


/**
 * Created by Rishabh Shukla
 * on 2019-08-19
 * Email : rishabh1450@gmail.com
 */

class SocialLoginChatSentAdapter(val context: Context, private val model: LoginChatMessageModel) : Item<ViewHolder>(){

    override fun getLayout() = R.layout.chat_message_sent_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_message_incoming.text = model.msg
    }
}