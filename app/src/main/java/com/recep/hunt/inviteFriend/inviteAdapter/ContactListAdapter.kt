package com.recep.hunt.inviteFriend.inviteAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.inviteFriend.model.ContactHistoryModel
import de.hdodenhof.circleimageview.CircleImageView


class ContactListAdapter(private val list: List<ContactHistoryModel>, private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<ContactListAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(item: ContactHistoryModel, position: Int)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val c = list[position]
        holder.tvFriendInvitUserNameId.setText(c.userName)
        holder.tvFriendInvitUserNumberId.setText(c.userNumber)
        if (c.userImg == null) {
            holder.civInviteFriendId.setImageResource(R.drawable.ic_person_gray_24dp)
        } else {
            holder.civInviteFriendId.setImageBitmap(c.userImg)
        }

        holder.rlInviteFriendClickId.setOnClickListener()
        {
            clickListener.onItemClick(list.get(position), position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_friend_invited_adapter, parent, false)
        return MyViewHolder(v)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvFriendInvitUserNumberId: TextView
        var tvFriendInvitUserNameId: TextView
        var civInviteFriendId: ImageView
        var rlInviteFriendClickId: RelativeLayout

        init {
            tvFriendInvitUserNumberId = view.findViewById<View>(R.id.tvFriendInvitUserNumberId) as TextView
            tvFriendInvitUserNameId = view.findViewById<View>(R.id.tvFriendInvitUserNameId) as TextView
            civInviteFriendId = view.findViewById<View>(R.id.civInviteFriendId) as CircleImageView
            rlInviteFriendClickId = view.findViewById<View>(R.id.rlInviteFriendClickId) as RelativeLayout

        }

    }
}

