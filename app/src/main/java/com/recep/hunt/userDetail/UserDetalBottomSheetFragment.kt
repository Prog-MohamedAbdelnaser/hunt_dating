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
import com.recep.hunt.utilis.FlowLayout
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.basic_info_btn_item.view.*
import kotlinx.android.synthetic.main.six_photos_item_layout.view.*
import kotlinx.android.synthetic.main.user_detail_bottom_sheet_layout.view.*
import kotlinx.android.synthetic.main.user_detail_header_item.view.*
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_detail_bottom_sheet_layout,container,false)
        basicInfoViewModel = ViewModelProviders.of(this).get(BasicInfoViewModel::class.java)
        init(view)
        return view
    }

    private fun init(view:View?){
        if(view != null){
            act = activity ?: return
            recyclerView = view.find(R.id.user_detail_recyclerView)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(ctx)
            recyclerView.addItemDecoration(SimpleDividerItemDecoration(ctx))
            setupAdapters()
        }
    }

    private fun setupAdapters(){
        adapter.add(UserDetailHeaderItem(ctx))
        adapter.add(UserDetailBasicInfoItem(basicInfoViewModel,act,ctx))
        adapter.add(UserDetailPhotoInfoItem(ctx))
    }


}





class UserDetailHeaderItem(private val ctx:Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_header_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val aboutYou = SharedPrefrenceManager.getAboutYou(ctx)
        val jobTitle = SharedPrefrenceManager.getJobTitle(ctx)
        val firstName = SharedPrefrenceManager.getUserFirstName(ctx)

            if(aboutYou != Constants.NULL)
                viewHolder.itemView.user_detail_about_you.text = aboutYou
            if(jobTitle != Constants.NULL)
                viewHolder.itemView.user_detail_job_title.text = jobTitle

        viewHolder.itemView.user_detail_username_txtView.text = firstName
    }
}

class UserDetailBasicInfoItem(private val basicInfoViewModel: BasicInfoViewModel,private val act:Activity,private val context: Context):Item<ViewHolder>(){
    private lateinit var flowLayout: FlowLayout
    override fun getLayout() = R.layout.user_detail_basic_info_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {
        flowLayout = viewHolder.itemView.find(R.id.user_detail_basic_info_layout)
        setupBasicInfoList()
    }
    private fun setupBasicInfoList(){
        val data = basicInfoViewModel.getData()
        for(model in data){
            if(model.questions.selectedValue != Constants.NULL){
                addChip(model)
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
        flowLayout.addView(newView)

    }
}

class UserDetailPhotoInfoItem(private val ctx:Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.user_detail_photos_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}