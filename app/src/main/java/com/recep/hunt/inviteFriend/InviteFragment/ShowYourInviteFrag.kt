package com.recep.hunt.inviteFriend.InviteFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.inviteFriend.model.InviteHistoryModel
import com.recep.hunt.inviteFriend.model.InviteHistoryStatus
import com.recep.hunt.inviteFriend.model.InvitesModel
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.my_credit_adapter_item.view.*
import kotlinx.android.synthetic.main.my_invited_adapter_item.view.*
import org.jetbrains.anko.find


class ShowYourInviteFrag : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()

    companion object {
        private lateinit var mctx: Context
        fun newInstance(context: Context): ShowYourInviteFrag {
            mctx = context
            return ShowYourInviteFrag()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.show_your_invite, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        recyclerView = view.find(R.id.rvShowYourInviteId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(mctx))
        for (model in getInviteHistoryData()) {
            adapter.add(InviteModleItems(model))
        }

    }

    private fun getInviteHistoryData(): ArrayList<InvitesModel> {
        val data = ArrayList<InvitesModel>()
        if (data.size == 0) {
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))
            data.add(InvitesModel("Recep@gmail.com", "singed up", "13 days ago"))

        }
        return data
    }

    class InviteModleItems(private val model: InvitesModel) : Item<ViewHolder>() {
        override fun getLayout() = R.layout.my_invited_adapter_item
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tvInvitedUserNameId.text = model.userName


        }
    }

}

