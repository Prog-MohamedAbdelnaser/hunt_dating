package com.recep.hunt.swipe.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.recep.hunt.R
import com.recep.hunt.base.adapter.BaseAdapter
import com.recep.hunt.base.adapter.BaseViewHolder
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.StoriesProgressView
import kotlinx.android.synthetic.main.swipe_screen_item.view.*
import org.jetbrains.anko.image

class SwipeProfiles: BaseAdapter<SwipeUserModel>(itemLayoutRes = R.layout.swipe_screen_item) {

    var userStoriesImageView:ImageView?=null

    var ivUserReact:ImageView?=null

    var activity:AppCompatActivity?=null

    private var currentStoriesPosition:Int=0

    var userModel:SwipeUserModel?=null

    private var clickToolbarControllerListener:ClickToolbarControllerListener?=null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val context=recyclerView.context

        if (context is ClickToolbarControllerListener){
            clickToolbarControllerListener = context
        }

    }

    fun getCurrentProfile(): SwipeUserModel {return userModel!!}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SwipeUserModel> {


        return SwipProfileViewHolder(getItemView(parent))
    }

    inner class SwipProfileViewHolder(view: View): BaseViewHolder<SwipeUserModel>(view),

        StoriesProgressView.StoriesListener {
        override fun onNext() {
            Log.i("StoriesListener","onNext")
            if (currentStoriesPosition < item?.images?.size?:0 -1){
                currentStoriesPosition++
                item?.images!![currentStoriesPosition]?.let {   loadImage(itemView.story_image_userdetail,it,itemView.context)}
            }
        }

        override fun onPrev() {
            if (currentStoriesPosition >0){
                currentStoriesPosition--
                item?.images!![currentStoriesPosition]?.let {   loadImage(itemView.story_image_userdetail,it,itemView.context)}
            }
        }

        override fun onComplete() {
            Log.i("StoriesListener","onComplete")

            currentStoriesPosition=item?.images?.size?:0
        }

        override fun fillData() {
                itemView.apply {

                    userModel=item
                    val storyProgressViews = stories

                    Log.w("SwipeProfiles", "user$adapterPosition ${item.toString()}")
                    //initialize matching Statue ProgressBar
                    val drawableMatch =
                        context.resources.getDrawable(R.drawable.circular_progressbar_inside_bg)
                    match_status_progressBar.progressDrawable = drawableMatch
                    match_status_progressBar.progress = item?.totalMatching?.toInt() ?: 0
                    match_status_progressBar.max = 100
                    textView50.text = "" + item?.totalMatching?.toInt()
                    textView51.text = "" + item?.totalMeeting
                    //initialize attendance status progressbar
                    val drawableAttendance =
                        context.resources.getDrawable(R.drawable.circular_progressbar_attendance_inside_bg)
                    attendance_status_progressBar.progressDrawable = drawableAttendance
                    attendance_status_progressBar.progress = item?.totalMeeting ?: 0
                    attendance_status_progressBar.max = 100


                    //initializ stories images
                    storyProgressViews.setStoriesCount(item?.images!!.size)
                    storyProgressViews.setStoryDuration(10000L)
                    storyProgressViews.setStoriesListener(this@SwipProfileViewHolder)
                    storyProgressViews.startStories()


                    user_detail_username_txtView.text =
                        item?.firstName + ", " + item?.age.toString()
                    user_detail_place_txtView.text = item?.locationName
                    user_detail_job_title.text = item?.title
                    user_detail_about_you.text = item?.detail

                    //initialize isIncognito button
                    val isIncognito = SharedPrefrenceManager.getisIncognito(this.context)
                    if (isIncognito) user_detail_incoginoti_btn.image =
                        resources.getDrawable(R.drawable.ghost_on)
                    else user_detail_incoginoti_btn.image = resources.getDrawable(R.drawable.ghost)

                    //initialize toolbar actions
                    user_detail_filter_btn.setOnClickListener { clickToolbarControllerListener?.onClickFilter() }
                    user_detail_incoginoti_btn.setOnClickListener {
                        clickToolbarControllerListener?.onClickShowIncognito()
                        val isIncognito = SharedPrefrenceManager.getisIncognito(this.context)
                        if (isIncognito) user_detail_incoginoti_btn.image =
                            resources.getDrawable(R.drawable.ghost)
                        else user_detail_incoginoti_btn.image =
                            resources.getDrawable(R.drawable.ghost_on)
                    }
                    user_detail_notification_btn.setOnClickListener { clickToolbarControllerListener?.onClickShowNotification() }
                    user_detail_profile_btn.setOnClickListener { clickToolbarControllerListener?.onClickShowProfile() }

                    //initialize filtration icons

                    if (item?.is_online == "true") {
                        ivStatusOnline.visibility = View.VISIBLE
                    }

                    if (item?.for_bussiness != null) {

                        if (item?.for_bussiness?.isNotEmpty()!!) {
                            ivForBusiness.visibility = View.VISIBLE
                        }
                    }

                    if (item?.for_date.isNullOrEmpty().not()) {
                        ivForDate.visibility = View.VISIBLE
                    }

                    if (item?.for_friendship.isNullOrEmpty().not()) {
                        ivForFriendship.visibility = View.VISIBLE
                    }

                    //
                    return_home_layout.setOnClickListener {
                        (context as Activity ).finish()
                    }

                    //
                    item?.images?.first()?.let { loadImage(story_image_userdetail, it,this.context) }

                    ivUserReact=like_dislike_imageView
                    userStoriesImageView  = story_image_userdetail


                }
            }

        }

    fun removeTop(){
        removeItem(0)
        notifyDataSetChanged()
    }

    fun isLastPosition()=getLastPosition()== getCurrentPosition()

    interface ClickToolbarControllerListener{
        fun onClickShowProfile()
        fun onClickShowIncognito()
        fun onClickShowNotification()
        fun onClickFilter()
    }

    fun loadImage(imageView: ImageView,imagePath:String,context:Context){
        Glide.with(context)
            .load(imagePath)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(imagePath))
            .into(imageView)
    }

    fun updateUserReactAction(isLike: Boolean?){
        if (isLike!=null) {
            ivUserReact?.alpha = 1.0f
            if (isLike) {
                ivUserReact?.setImageResource(R.drawable.swipe_like)
            } else {
                ivUserReact?.setImageResource(R.drawable.swipe_dislike)
            }
        }else{
            ivUserReact?.alpha=0f
        }
    }
}