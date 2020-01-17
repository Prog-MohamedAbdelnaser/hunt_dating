package com.recep.hunt.userDetail

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.constants.Constants
import com.recep.hunt.model.ReportUser
import com.recep.hunt.model.reportUser.ReportUserResponse
import com.recep.hunt.profile.model.UserBasicInfoModel
import com.recep.hunt.profile.viewmodel.BasicInfoViewModel
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.userDetail.models.TimelineModel
import com.recep.hunt.utilis.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.delete_account_reason_dialog_layout.*
import kotlinx.android.synthetic.main.dont_want_to_join_hunt_dialog.*
import kotlinx.android.synthetic.main.dont_want_to_join_hunt_dialog.reason6_btn
import kotlinx.android.synthetic.main.dont_want_to_join_hunt_dialog.tvTitleReasons
import kotlinx.android.synthetic.main.my_top_user_artist_item.view.*
import kotlinx.android.synthetic.main.number_changed_success_layout.*
import kotlinx.android.synthetic.main.report_profile_item_layout.view.*
import kotlinx.android.synthetic.main.report_user_question_dialog_layout.*
import kotlinx.android.synthetic.main.report_user_reasons_dailog_layout.*
import kotlinx.android.synthetic.main.six_photos_item_layout.view.*
import kotlinx.android.synthetic.main.user_detail_header_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

class UserDetalBottomSheetFragment(private val ctx: Context) : BottomSheetDialogFragment() {

    constructor(context: Context, swipeUserModel: SwipeUserModel) : this(context) {
        this.swipeUserModel = swipeUserModel;
    }

    var swipeUserModel: SwipeUserModel? = null
    private lateinit var act: Activity
    private lateinit var basicInfoViewModel: BasicInfoViewModel
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    private var basicInfo = ArrayList<UserBasicInfoModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_detail_bottom_sheet_layout, container, false)
        init(view)

        return view
    }

    private fun init(view: View?) {
        if (view != null) {
            act = activity ?: return
            basicInfoViewModel = BasicInfoViewModel.getInstace(act.application)
            if (basicInfo.size == 0) {
                basicInfo = if (swipeUserModel != null) {
                    basicInfoViewModel.getData(swipeUserModel?.basicInfo)
                } else
                    basicInfoViewModel.getData()
            }
            recyclerView = view.find(R.id.user_detail_recyclerView)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(ctx)
            recyclerView.addItemDecoration(SimpleDividerItemDecoration(ctx))
            setupAdapters()
        }
    }


    private fun setupAdapters() {
        val images = getUserImages(swipeUserModel)
        adapter.add(UserDetailHeaderItem(ctx, swipeUserModel))

        adapter.add(UserDetailBasicInfoItem(basicInfo, act, ctx))
        images?.let { UserDetailPhotoInfoItem(it, ctx) }?.let { adapter.add(it) }
        // adapter.add(UserDetailFacebookItem())
        // adapter.add(UserDetailInstagramItem())
        adapter.add(UserDetailExperienceItem(ctx, getExperienceTimeline()))
        adapter.add(UserEducationItem(ctx, getEducationTimeline()))
        //adapter.add(UserSpotifyTopArtistItem(ctx, getMyTopArtists()))
//        adapter.add(ReportProfileItem(SharedPrefrenceManager.getUserFirstName(ctx),requireActivity(),swipeUserModel))
    }

    private fun getExperienceTimeline(): ArrayList<TimelineModel> {
        val experienceModel = ArrayList<TimelineModel>()
        if (experienceModel.size == 0) {
            experienceModel.add(TimelineModel("Android and iOS Developer", "Present"))
            experienceModel.add(TimelineModel("iOS Developer", "2 years, 2019"))
            experienceModel.add(TimelineModel("Android Developer", "1 year, 2018"))

        }
        return experienceModel
    }

    private fun getEducationTimeline(): ArrayList<TimelineModel> {
        val educationModel = ArrayList<TimelineModel>()
        if (educationModel.size == 0) {
            educationModel.add(
                TimelineModel(
                    "Bachelor of Technology, in Computer Science & Engineering ",
                    "2014-2018"
                )
            )
            educationModel.add(TimelineModel("12th CBSE board", "2014"))
        }
        return educationModel
    }

    private fun getMyTopArtists(): ArrayList<String> {
        val artists = ArrayList<String>()
        if (artists.size == 0) {
            artists.add("Arijit Singh")
            artists.add("Pitbull")
            artists.add("Chainsmokers")
            artists.add("Hardwell")
            artists.add("Shawn Mendez")
        }
        return artists
    }

    private fun getUserImages(swipeUserModel: SwipeUserModel?): ArrayList<String>? {

        val images = arrayListOf<String>()
        if (swipeUserModel != null) {
            return swipeUserModel.images
        }
        val image1 = SharedPrefrenceManager.getFirstImg(ctx)
        val image2 = SharedPrefrenceManager.getSecImg(ctx)
        val image3 = SharedPrefrenceManager.getThirdImg(ctx)
        val image4 = SharedPrefrenceManager.getFourthImg(ctx)
        val image5 = SharedPrefrenceManager.getFiveImg(ctx)
        val image6 = SharedPrefrenceManager.getSixImg(ctx)

        if (image1 != Constants.NULL)
            images.add(image1)
        if (image2 != Constants.NULL)
            images.add(image2)
        if (image3 != Constants.NULL)
            images.add(image3)
        if (image4 != Constants.NULL)
            images.add(image4)
        if (image5 != Constants.NULL)
            images.add(image5)
        if (image6 != Constants.NULL)
            images.add(image6)

        return images
    }


}

class UserDetailHeaderItem(private val ctx: Context, val swipeUserModel: SwipeUserModel?) :
    Item<ViewHolder>() {
    override fun getLayout() = R.layout.user_detail_header_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        var aboutYou: String? = null;
        var jobTitle: String? = null
        var firstName: String? = null
        var age: String? = null

        if (swipeUserModel != null) {
            aboutYou = swipeUserModel.detail
            jobTitle = ""
            firstName = swipeUserModel.firstName
            age = swipeUserModel.age.toString()
        } else {
            aboutYou = SharedPrefrenceManager.getAboutYou(ctx)
            jobTitle = SharedPrefrenceManager.getJobTitle(ctx)
            firstName = SharedPrefrenceManager.getUserFirstName(ctx)
            age = SharedPrefrenceManager.getUserAge(ctx)
        }
        if (aboutYou != Constants.NULL)
            viewHolder.itemView.user_detail_about_you.text = aboutYou
        if (jobTitle != Constants.NULL)
            viewHolder.itemView.user_detail_job_title.text = jobTitle

        if (age == null) {
            age = "18"
        }
        viewHolder.itemView.user_detail_username_txtView.text = "$firstName, $age"
    }
}

class UserDetailBasicInfoItem(
    private val basicInfoViewModel: ArrayList<UserBasicInfoModel>,
    private val act: Activity,
    private val context: Context
) : Item<ViewHolder>() {
    private lateinit var flowLayout: FlowLayout
    override fun getLayout() = R.layout.user_detail_basic_info_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        flowLayout = viewHolder.itemView.find(R.id.user_detail_basic_info_layout)
        setupBasicInfoList()
    }

    private fun setupBasicInfoList() {
        if (flowLayout.childCount == 0) {
            for (model in basicInfoViewModel) {
                if (model.questions.selectedValue != Constants.NULL) {
                    addChip(model)
                }
            }
        } else {
            flowLayout.removeAllViews()
            for (model in basicInfoViewModel) {
                if (model.questions.selectedValue != Constants.NULL) {
                    addChip(model)
                }
            }
        }

    }

    private fun addChip(model: UserBasicInfoModel) {
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

class UserDetailPhotoInfoItem(private val images: ArrayList<String>, private val ctx: Context) :
    Item<ViewHolder>() {
    override fun getLayout() = R.layout.user_detail_photos_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        for (i in 0 until images.size) {
            val image = images[i]
            when (i) {
                0 -> {

                    if (image.contains("http")) {
                        Glide.with(ctx)
                            .load(image)
                            .placeholder(R.drawable.add_image)
                            .into(viewHolder.itemView.user_image_1)
                    } else {
                        viewHolder.itemView.user_image_1.setImageBitmap(
                            StringToBitmap(
                                image
                            )
                        )
                    }
                }
                1 -> {

                    if (image.contains("http")) {
                        Glide.with(ctx)
                            .load(image)
                            .placeholder(R.drawable.add_image)
                            .into(viewHolder.itemView.user_image_2)
                    } else {
                        viewHolder.itemView.user_image_2.setImageBitmap(
                            StringToBitmap(
                                image
                            )
                        )
                    }
                }
                2 -> {

                    if (image.contains("http")) {
                        Glide.with(ctx)
                            .load(image)
                            .placeholder(R.drawable.add_image)
                            .into(viewHolder.itemView.user_image_3)
                    } else {
                        viewHolder.itemView.user_image_3.setImageBitmap(
                            StringToBitmap(
                                image
                            )
                        )
                    }
                }
                3 -> {

                    if (image.contains("http")) {
                        Glide.with(ctx)
                            .load(image)
                            .placeholder(R.drawable.add_image)
                            .into(viewHolder.itemView.user_image_4)
                    } else {
                        viewHolder.itemView.user_image_4.setImageBitmap(
                            StringToBitmap(
                                image
                            )
                        )
                    }
                }
                5 -> {

                    if (image.contains("http")) {
                        Glide.with(ctx)
                            .load(image)
                            .placeholder(R.drawable.add_image)
                            .into(viewHolder.itemView.user_image_5)
                    } else {
                        viewHolder.itemView.user_image_5.setImageBitmap(
                            StringToBitmap(
                                image
                            )
                        )
                    }
                }
            }


        }
    }
}

fun StringToBitmap(img: String): Bitmap? {
    var bitmap: Bitmap? = null
    if (img != null) {
        var b = Base64.decode(img, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
    }
    return bitmap
}

class UserDetailFacebookItem : Item<ViewHolder>() {
    override fun getLayout() = R.layout.user_detail_fb_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class UserDetailInstagramItem : Item<ViewHolder>() {
    override fun getLayout() = R.layout.user_detail_insta_photos_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class UserSpotifyTopArtistItem(
    private val context: Context,
    private val myTopSpotifyArtists: ArrayList<String>
) : Item<ViewHolder>() {
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun getLayout() = R.layout.my_top_spotify_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        recyclerView = viewHolder.itemView.find(R.id.my_top_spotify_artists_recylerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        for (artist in myTopSpotifyArtists)
            adapter.add(MyTopUserArtistItem(artist))

    }
}

class MyTopUserArtistItem(private val name: String) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.my_top_user_artist_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.my_top_artist_name.text = name
    }
}

class ReportProfileItem(private val userName: String, private val ctx: Activity, private val swipeUserModel: SwipeUserModel?) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.report_profile_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.report_profile_btn_text.text = "Report ${swipeUserModel?.firstName}'s Profile"

        viewHolder.itemView.reportProfileLout.setOnClickListener {
            askReportUser()
        }
        viewHolder.itemView.report_profile_btn_text.setOnClickListener {
            viewHolder.itemView.reportProfileLout.performClick()
        }
    }

    private fun askReportUser() {
        val ll =
            LayoutInflater.from(ctx).inflate(R.layout.report_user_question_dialog_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.btnNo.setOnClickListener { dialog.dismiss() }
        dialog.btnYes.setOnClickListener {
            reportAccountDialog()
            dialog.dismiss()
        }

        dialog.show()

    }



    private fun reportUser(resone:String) {

        val reportUserModel= ReportUser(swipeUserModel?.id.toString(),resone)

        val call = ApiClient.getClient.reportUser(reportUserModel)

        call.enqueue(object: Callback<ReportUserResponse> {
            override fun onFailure(call: Call<ReportUserResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ReportUserResponse>,
                response: Response<ReportUserResponse>
            ) {
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(ctx, strErrorJson)) {
                        return
                    }
                }

                reportAccountSuccessDialog(ctx)
            }

        })

    }

    private fun reportAccountDialog() {
        val ll = LayoutInflater.from(ctx).inflate(R.layout.report_user_reasons_dailog_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.tvTitleReasons.text=ctx.getString(R.string.report_user)

        dialog.tvInappropriateContent.setOnClickListener {
            reportUser( dialog.tvInappropriateContent.text.toString())
            dialog.dismiss()
        }
        dialog.tvSexualPicture.setOnClickListener {
            reportUser( dialog.tvSexualPicture.text.toString())

            dialog.dismiss()
        }
        dialog.tvSpamOrAdvertising.setOnClickListener {

            reportUser( dialog.tvSpamOrAdvertising.text.toString())

            dialog.dismiss()

        }
        dialog.tvHarrassment.setOnClickListener {
            reportUser( dialog.tvHarrassment.text.toString())

            dialog.dismiss()
        }
        dialog.tvFraud.setOnClickListener {
            reportUser( dialog.tvFraud.text.toString())

            dialog.dismiss()
        }
        dialog.tvOtherReasons.setOnClickListener {

            dialog.dismiss()
            otherReasonDialog()
        }
        dialog.show()
    }


    private fun otherReasonDialog() {

        val ll =
            LayoutInflater.from(ctx).inflate(R.layout.delete_account_reason_dialog_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.tvOtherReasonsTitle.text=ctx.getString(R.string.report_user)
        dialog.delete_account_back_btn.setOnClickListener { dialog.dismiss()
            reportAccountDialog()
        }
        dialog.delete_account_submit_btn.setOnClickListener {

            if (dialog. inputReason.text.toString().isNullOrEmpty().not()){
                reportUser(dialog. inputReason.text.toString())
                dialog.dismiss()
            }else{
                dialog.inputReason.error="Enter Reason !"
            }


        }
        dialog. inputReason.addTextChangedListener {
            dialog.inputReason.error=null
        }

        dialog.show()

    }

    private fun reportAccountSuccessDialog(ctx: Context) {

        val ll = LayoutInflater.from(ctx).inflate(R.layout.number_changed_success_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.number_change_success_dialog_title.text =
            ctx.resources.getText(R.string.you_have_successfully_deactivated)
        dialog.lottieAnimationView2.playAnimation()
        dialog.ok_btn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}