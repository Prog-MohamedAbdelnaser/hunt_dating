package com.recep.hunt.filters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.recep.hunt.R
import com.recep.hunt.filters.model.LookingForDataModel
import org.jetbrains.anko.find
import org.jetbrains.anko.image


/**
 * Created by Rishabh Shukla
 * on 2019-08-28
 * Email : rishabh1450@gmail.com
 */

class FilterAdapter(private val context: Context,private val model:ArrayList<LookingForDataModel>) : PagerAdapter(){

    private lateinit var title:TextView
    private lateinit var icon:ImageView
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(context).inflate(R.layout.filter_adapter_item, container, false)
        container.addView(item)
        title = item.find(R.id.filter_adapter_item_title)
        icon = item.find(R.id.filter_adapter_item_image)

        title.text = model[position].title
        icon.image = context.resources.getDrawable(model[position].selectedImage)
        return item
    }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}