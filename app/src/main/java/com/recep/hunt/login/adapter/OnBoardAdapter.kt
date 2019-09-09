package com.recep.hunt.login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.recep.hunt.R


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class OnBoardAdapter(private val context: Context, private val images:ArrayList<Int>) : PagerAdapter(){
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(context).inflate(R.layout.on_board_adapter, container, false)
        //adapter imageView - change it with positions
//        val adapterImage : ImageView = item.find(R.id.on_board_image_View)
        container.addView(item)
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