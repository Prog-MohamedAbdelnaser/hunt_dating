package com.recep.hunt.swipe

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.recep.hunt.R
import com.recep.hunt.swipe.adapters.SwipeScreenAdapter
import com.recep.hunt.swipe.model.SwipeUserModel
import kotlinx.android.synthetic.main.activity_swipe.*
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource

class SwipeActivity : AppCompatActivity() {

    private lateinit var flingContainer: SwipeFlingAdapterView
    private lateinit var adapter: SwipeScreenAdapter
//    private var userImagesStoriesData = ArrayList<String>()
    private var items =  ArrayList<SwipeUserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)
        init()
    }

    private fun init() {

        //First story will be only loaded to swiping container
        flingContainer = find(R.id.swipe_screen_frame)
        items = dummyUsersdata()
        adapter = SwipeScreenAdapter(this, items)
        flingContainer.adapter = adapter

        flingContainer.setFlingListener(object: SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {

            }

            override fun onLeftCardExit(p0: Any?) {
                //If card is removed, then new story will be loaded as well
                items.removeAt(1)
                items.removeAt(0)
                items.add(
                    SwipeUserModel(
                        "Stella, 28",
                        "Model at Fashion Club"
                    )
                )
                adapter.notifyDataSetChanged()
            }

            override fun onRightCardExit(p0: Any?) {
                //If card is removed, then new story will be loaded as well
                items.removeAt(1)
                items.removeAt(0)
                items.add(
                    SwipeUserModel(
                        "Stella, 28",
                        "Model at Fashion Club"
                    )
                )
                adapter.notifyDataSetChanged()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                adapter.notifyDataSetChanged()
            }

            override fun onScroll(scrollProgressPercent: Float) {
                val view = flingContainer.selectedView
                if (scrollProgressPercent > 0) {
                    view.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 1.0f
                    view.findViewById<ImageView>(R.id.like_dislike_imageView).imageResource = R.drawable.swipe_like
                    view.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb(179, 58, 204, 225))
                    //If user is dragging card now, next story will be ready for showing
                    if (items.size == 1) {
                        items.add(
                            SwipeUserModel(
                                "Stella, 28",
                                "Model at Fashion Club"
                            )
                        )
                        adapter.notifyDataSetChanged()
                    }
                }

                else if (scrollProgressPercent < 0) {
                    view.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 1.0f
                    view.findViewById<ImageView>(R.id.like_dislike_imageView).imageResource = R.drawable.swipe_dislike
                    view.findViewById<ImageView>(R.id.story_image_userdetail).setColorFilter(Color.argb(153, 255, 42, 78))
                    //If user is dragging card now, next story will be ready for showing
                    if (items.size == 1) {
                        items.add(
                            SwipeUserModel(
                                "Stella, 28",
                                "Model at Fashion Club"
                            )
                        )
                        adapter.notifyDataSetChanged()
                    }
                }
                else {
                    view.findViewById<ImageView>(R.id.like_dislike_imageView).alpha = 0.0f
                    view.findViewById<ImageView>(R.id.story_image_userdetail).colorFilter = null
                    //If user didn't pass card away and swiping container has 2 stories, remove last one as well
                    if (items.size > 1) {
                        items.removeAt(1)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener { i, any ->
            val view = flingContainer.selectedView
//            view.findViewById<View>(R.id.reverse_detail).setOnClickListener {
//                adapter.prevImage(i)
//            }
//            view.findViewById<View>(R.id.skip_detail).setOnClickListener {
//                adapter.nextImage()
//            }
        }


//        userImagesStoriesData = getStoryData()
    }


    /*
    private fun getStoryData():ArrayList<String> {
        val userImagesStoriesData = ArrayList<String>()

        val firstImage = SharedPrefrenceManager.getFirstImg(this)
        val secondImage = SharedPrefrenceManager.getSecImg(this)
        val thirdImage = SharedPrefrenceManager.getThirdImg(this)
        val fourthImage = SharedPrefrenceManager.getFourthImg(this)
        val fifthImage = SharedPrefrenceManager.getFiveImg(this)
        val sixthImage = SharedPrefrenceManager.getSixImg(this)
        val userImage = SharedPrefrenceManager.getProfileImg(this)

        if(userImage != Constants.NULL)
            userImagesStoriesData.add(userImage)
        if(firstImage != Constants.NULL)
            userImagesStoriesData.add(firstImage)
        if(secondImage != Constants.NULL)
            userImagesStoriesData.add(secondImage)
        if(thirdImage != Constants.NULL)
            userImagesStoriesData.add(thirdImage)
        if(fourthImage != Constants.NULL)
            userImagesStoriesData.add(fourthImage)
        if(fifthImage != Constants.NULL)
            userImagesStoriesData.add(fifthImage)
        if(sixthImage != Constants.NULL)
            userImagesStoriesData.add(sixthImage)

        userImagesStoriesData.add("")

        return userImagesStoriesData
    }
    */

    override fun onDestroy() {
//        storyProgressView.destroy()
        super.onDestroy()
    }

    //Dummy Users Data
    private fun dummyUsersdata():ArrayList<SwipeUserModel>{
        val data = ArrayList<SwipeUserModel>()
        if(data.size == 0){
            data.add(
                SwipeUserModel(
                    "Valentina, 28",
                    "Actor at Max Studio"
                )
            )
//            data.add(
//                SwipeUserModel(
//                    "Stella, 28",
//                    "Model at Fashion Club"
//                )
//            )
//            data.add(
//                SwipeUserModel(
//                    "Serena, 23",
//                    "Digital Artist at Blue Tea Productions"
//                )
//            )
        }
        return data
    }

}
