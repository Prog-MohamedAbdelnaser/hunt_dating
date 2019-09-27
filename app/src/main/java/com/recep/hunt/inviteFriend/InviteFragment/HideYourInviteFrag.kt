package com.recep.hunt.inviteFriend.InviteFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.recep.hunt.R

class HideYourInviteFrag : Fragment() {
    companion object {
        private lateinit var mctx: Context
        fun newInstance(context: Context): HideYourInviteFrag {
            mctx = context
            return HideYourInviteFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hide_your_invite, container, false)
    }
}
