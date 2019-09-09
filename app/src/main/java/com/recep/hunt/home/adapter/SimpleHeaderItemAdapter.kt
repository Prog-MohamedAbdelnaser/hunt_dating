package com.recep.hunt.home.adapter

import com.recep.hunt.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.simple_header_item.view.*


/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

class SimpleHeaderItemAdapter(val text:String): Item<ViewHolder>() {

    override fun getLayout() = R.layout.simple_header_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.simple_header_title.text = text
    }
}