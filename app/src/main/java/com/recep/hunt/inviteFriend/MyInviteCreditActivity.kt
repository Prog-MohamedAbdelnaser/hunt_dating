package com.recep.hunt.inviteFriend

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.inviteFriend.model.InviteHistoryModel
import com.recep.hunt.inviteFriend.model.InviteHistoryStatus
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.my_credit_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor

class MyInviteCreditActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_invite_credit)
        init()
    }
    private fun init(){
        recyclerView = find(R.id.my_invite_credit_recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        for (model in getInviteHistoryData()){
            adapter.add(InviteHistoryItems(model))
        }
    }
    private fun getInviteHistoryData():ArrayList<InviteHistoryModel>{
        val data = ArrayList<InviteHistoryModel>()
        if(data.size == 0){
            data.add(InviteHistoryModel("Rishabh Shukla",status = InviteHistoryStatus.PENDING))
            data.add(InviteHistoryModel("Ashley Lulia",status = InviteHistoryStatus.APPROVED))
            data.add(InviteHistoryModel("Likhon",status = InviteHistoryStatus.PENDING))
            data.add(InviteHistoryModel("Recep",status = InviteHistoryStatus.PENDING))
            data.add(InviteHistoryModel("Sahil",status = InviteHistoryStatus.APPROVED))
        }
        return data
    }
}
class InviteHistoryItems(private val model: InviteHistoryModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.my_credit_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.invite_history_userName.text = model.userName

        when(model.status){
            InviteHistoryStatus.APPROVED->{
                viewHolder.itemView.cancel_invite_btn.visibility = View.GONE
                viewHolder.itemView.invite_history_you_got_bth_txt.visibility = View.VISIBLE
                viewHolder.itemView.invite_history_got_amount.text = "$ 1"
                viewHolder.itemView.invite_history_got_amount.textColor = Color.parseColor("#03D57A")
            }
            else->{
                viewHolder.itemView.cancel_invite_btn.visibility = View.VISIBLE
                viewHolder.itemView.invite_history_you_got_bth_txt.visibility = View.GONE
                viewHolder.itemView.invite_history_got_amount.text = "$ 0"
                viewHolder.itemView.invite_history_got_amount.textColor = Color.parseColor("#9A9DA4")
            }
        }


    }
}
