package com.recep.hunt.userDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.recep.hunt.R
import com.recep.hunt.userDetail.models.TimelineModel
import com.recep.hunt.utilis.VectorDrawableUtils
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.timeline_view_item.view.*
import org.jetbrains.anko.find


/**
 * Created by Rishabh Shukla
 * on 2019-09-20
 * Email : rishabh1450@gmail.com
 */

class UserDetailExperienceItem(private val context:Context,private val experienceModel:ArrayList<TimelineModel>): Item<ViewHolder>(){
    private lateinit var recyclerView:RecyclerView
    override fun getLayout() = R.layout.user_detail_experience_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        recyclerView = viewHolder.itemView.find(R.id.user_detail_experience_recyclerView)
        recyclerView.adapter = TimelineAdapter(experienceModel)
        recyclerView.layoutManager = LinearLayoutManager(context)

    }
}
class UserEducationItem(private val context:Context,private val educationModel:ArrayList<TimelineModel>):Item<ViewHolder>(){
    private lateinit var recyclerView:RecyclerView
    override fun getLayout() = R.layout.user_detail_education_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        recyclerView = viewHolder.itemView.find(R.id.education_timeline_recyclerView)
        recyclerView.adapter = TimelineAdapter(educationModel)
        recyclerView.layoutManager = LinearLayoutManager(context)

    }
}
class TimelineAdapter(private val timeLineModel:ArrayList<TimelineModel>):RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        return TimeLineViewHolder(layoutInflater.inflate(R.layout.timeline_view_item, parent, false), viewType)
    }
    override fun getItemCount() = timeLineModel.size

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val model = timeLineModel[position]
        holder.date.text = model.title
        holder.message.text = model.message
       // setMarker(holder, R.drawable.notification_round_btn_bg, R.color.colorPrimaryDark)
    }
    private fun setMarker(holder: TimeLineViewHolder, drawableResId: Int, colorFilter: Int) {
        holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
    }

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.text_timeline_date
        val message = itemView.text_timeline_title
        val timeline = itemView.timeline
        init {
            timeline.initLine(viewType)
        }
    }

}