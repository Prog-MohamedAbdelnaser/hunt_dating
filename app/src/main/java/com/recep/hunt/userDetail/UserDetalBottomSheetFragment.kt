package com.recep.hunt.userDetail

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.recep.hunt.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.recep.hunt.constants.Constants
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.userDetail.models.TimelineModel
import com.recep.hunt.utilis.FlowLayout
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.basic_info_btn_item.view.*
import kotlinx.android.synthetic.main.my_top_user_artist_item.view.*
import kotlinx.android.synthetic.main.report_profile_item_layout.view.*
import kotlinx.android.synthetic.main.six_photos_item_layout.view.*
import kotlinx.android.synthetic.main.user_detail_bottom_sheet_layout.view.*
import kotlinx.android.synthetic.main.user_detail_header_item.view.*
import kotlinx.coroutines.flow.flow
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.support.v4.find


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

class UserDetalBottomSheetFragment(private val ctx:Context) : BottomSheetDialogFragment() {

    private lateinit var act:Activity
    private lateinit var basicInfoViewModel:BasicInfoViewModel
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    private var basicInfo = ArrayList<UserBasicInfoModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_detail_bottom_sheet_layout,container,false)
        init(view)
        return view
    }

    private fun init(view:View?){
        if(view != null){
            act = activity ?: return
            basicInfoViewModel = BasicInfoViewModel.getInstace(act.application)
            if(basicInfo.size == 0)
                basicInfo = basicInfoViewModel.getData()

            recyclerView = view.find(R.id.user_detail_recyclerView)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(ctx)
            recyclerView.addItemDecoration(SimpleDividerItemDecoration(ctx))
            setupAdapters()

        }
    }


    private fun setupAdapters(){
        val images = getUserImages()
        adapter.add(UserDetailHeaderItem(ctx))
        adapter.add(UserDetailBasicInfoItem(basicInfo,act,ctx))
        adapter.add(UserDetailPhotoInfoItem(images,ctx))
        adapter.add(UserDetailFacebookItem())
        adapter.add(UserDetailInstagramItem())
        adapter.add(UserDetailExperienceItem(ctx,getExperienceTimeline()))
        adapter.add(UserEducationItem(ctx,getEducationTimeline()))
        adapter.add(UserSpotifyTopArtistItem(ctx,getMyTopArtists()))
        adapter.add(ReportProfileItem(SharedPrefrenceManager.getUserFirstName(ctx)))
    }
    private fun getExperienceTimeline():ArrayList<TimelineModel>{
        val experienceModel = ArrayList<TimelineModel>()
        if(experienceModel.size == 0){
            experienceModel.add(TimelineModel("Android and iOS Developer","Present"))
            experienceModel.add(TimelineModel("iOS Developer","2 years, 2019"))
            experienceModel.add(TimelineModel("Android Developer","1 year, 2018"))

        }
        return experienceModel
    }

    private fun getEducationTimeline():ArrayList<TimelineModel>{
        val educationModel = ArrayList<TimelineModel>()
        if(educationModel.size == 0){
            educationModel.add(TimelineModel("Bachelor of Technology, in Computer Science & Engineering ","2014-2018"))
            educationModel.add(TimelineModel("12th CBSE board","2014"))
        }
        return educationModel
    }
    private fun getMyTopArtists():ArrayList<String>{
        val artists = ArrayList<String>()
        if(artists.size == 0){
            artists.add("Arijit Singh")
            artists.add("Pitbull")
            artists.add("Chainsmokers")
            artists.add("Hardwell")
            artists.add("Shawn Mendez")
        }
        return artists
    }

    private fun getUserImages():ArrayList<String>{
        val image1 = SharedPrefrenceManager.getFirstImg(ctx)
        val image2 = SharedPrefrenceManager.getSecImg(ctx)
        val image3 = SharedPrefrenceManager.getThirdImg(ctx)
        val image4 = SharedPrefrenceManager.getFourthImg(ctx)
        val image5 = SharedPrefrenceManager.getFiveImg(ctx)
        val image6 = SharedPrefrenceManager.getSixImg(ctx)

        val images = arrayListOf<String>()

        if(image1 != Constants.NULL)
            images.add(image1)
        if(image2 != Constants.NULL)
            images.add(image2)
        if(image3 != Constants.NULL)
            images.add(image3)
        if(image4 != Constants.NULL)
            images.add(image4)
        if(image5 != Constants.NULL)
            images.add(image5)
        if(image6 != Constants.NULL)
            images.add(image6)

        return images
    }


}

class UserDetailHeaderItem(private val ctx:Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_header_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val aboutYou = SharedPrefrenceManager.getAboutYou(ctx)
        val jobTitle = SharedPrefrenceManager.getJobTitle(ctx)
        val firstName = SharedPrefrenceManager.getUserFirstName(ctx)
        var age = SharedPrefrenceManager.getUserAge(ctx)

            if(aboutYou != Constants.NULL)
                viewHolder.itemView.user_detail_about_you.text = aboutYou
            if(jobTitle != Constants.NULL)
                viewHolder.itemView.user_detail_job_title.text = jobTitle

        if(age==null)
        {
            age="18"
        }
        viewHolder.itemView.user_detail_username_txtView.text = "$firstName, $age"
    }
}

class UserDetailBasicInfoItem(private val basicInfoViewModel: ArrayList<UserBasicInfoModel>,private val act:Activity,private val context: Context):Item<ViewHolder>(){
    private lateinit var flowLayout: FlowLayout
    override fun getLayout() = R.layout.user_detail_basic_info_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        flowLayout = viewHolder.itemView.find(R.id.user_detail_basic_info_layout)
        setupBasicInfoList()
    }
    private fun setupBasicInfoList(){
        if(flowLayout.childCount == 0){
            for(model in basicInfoViewModel){
                if(model.questions.selectedValue != Constants.NULL){
                    addChip(model)
                }
            }
        }else{
            flowLayout.removeAllViews()
            for(model in basicInfoViewModel){
                if(model.questions.selectedValue != Constants.NULL){
                    addChip(model)
                }
            }
        }

    }
    private fun addChip(model:UserBasicInfoModel) {
        val layoutInflater = act.baseContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newView = layoutInflater.inflate(R.layout.basic_info_btn_item, null)
        val title = newView.find<TextView>(R.id.basic_info_btn_title)
        val icon = newView.find<ImageView>(R.id.basic_info_btn_image)

        title.text = model.questions.selectedValue
        icon.image = context.resources.getDrawable(model.icon)
//        flowLayout.removeAllViews()
        flowLayout.addView(newView)


    }
}

class UserDetailPhotoInfoItem(private val images:ArrayList<String>,private val ctx:Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_photos_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class UserDetailFacebookItem:Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_fb_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
class UserDetailInstagramItem:Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_insta_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
class UserSpotifyTopArtistItem(private val context: Context,private val myTopSpotifyArtists:ArrayList<String>):Item<ViewHolder>(){
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun getLayout() = R.layout.my_top_spotify_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        recyclerView = viewHolder.itemView.find(R.id.my_top_spotify_artists_recylerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        for(artist in myTopSpotifyArtists)
            adapter.add(MyTopUserArtistItem(artist))

    }
}
class MyTopUserArtistItem(private val name:String): Item<ViewHolder>(){
    override fun getLayout() = R.layout.my_top_user_artist_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.my_top_artist_name.text = name
    }
}

class ReportProfileItem(private val userName:String):Item<ViewHolder>(){
    override fun getLayout() = R.layout.report_profile_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.report_profile_btn_text.text = "Report $userName's Profile"
    }
}