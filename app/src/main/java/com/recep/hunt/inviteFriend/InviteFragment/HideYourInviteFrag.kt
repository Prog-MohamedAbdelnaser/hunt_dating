package com.recep.hunt.inviteFriend.InviteFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import org.jetbrains.anko.find
import android.widget.TextView


class HideYourInviteFrag : Fragment(), View.OnClickListener {

    private lateinit var rlHideInviteFbId: RelativeLayout
    private lateinit var rlHideInviteInstaId: RelativeLayout
    private lateinit var rlHideInviteTwitterId: RelativeLayout
    private lateinit var rlHideInviteMoreId: RelativeLayout
    private lateinit var tvHideInviteCopyId: TextView


    companion object {
        private lateinit var mctx: Context
        fun newInstance(context: Context): HideYourInviteFrag {
            mctx = context
            return HideYourInviteFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.hide_your_invite, container, false)
        init(view)
        return view
    }


    private fun init(view: View) {
        rlHideInviteFbId = view.find(R.id.rlHideInviteFbId)
        rlHideInviteInstaId = view.find(R.id.rlHideInviteInstaId)
        rlHideInviteTwitterId = view.find(R.id.rlHideInviteTwitterId)
        rlHideInviteMoreId = view.find(R.id.rlHideInviteMoreId)
        tvHideInviteCopyId = view.find(R.id.tvHideInviteCopyId)


        rlHideInviteFbId.setOnClickListener(this)
        rlHideInviteInstaId.setOnClickListener(this)
        rlHideInviteTwitterId.setOnClickListener(this)
        rlHideInviteMoreId.setOnClickListener(this)
        tvHideInviteCopyId.setOnClickListener(this)

    }

    override fun onClick(id: View?) {
        when (id!!.id) {
            R.id.tvHideInviteCopyId -> {
            }
            R.id.rlHideInviteFbId -> {

            }
            R.id.rlHideInviteInstaId -> {

            }
            R.id.rlHideInviteTwitterId -> {
            }
            R.id.rlHideInviteMoreId -> {
            }

        }
    }
}
