package com.recep.hunt.inviteFriend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.inviteFriend.InviteFragment.HideYourInviteFrag
import com.recep.hunt.inviteFriend.InviteFragment.ShowYourInviteFrag
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_invite_afriend.*
import org.jetbrains.anko.find

class InviteAFriendActivity : AppCompatActivity() {

    var flag: Boolean? = false
    private lateinit var tvShowHideInvitesid: TextView
    private lateinit var rlHideShowId: RelativeLayout
    private lateinit var view9: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_afriend)
        init()
    }

    private fun init() {
        view9 = find(R.id.view9)
        tvShowHideInvitesid = find(R.id.tvShowHideInvitesid)
        rlHideShowId = find(R.id.rlHideShowId)
        //default fragemnt setup
        setupFrag(HideYourInviteFrag.newInstance(this@InviteAFriendActivity));

        send_invite_btn.setOnClickListener {
            launchActivity<MyInviteCreditActivity>()
        }


        rlHideShowId.setOnClickListener()
        {
            // rlHideShowId.setBackgroundResource(R.drawable.invites_card)
            tvShowHideInvitesid.setText("Hide your invites")
            view9.visibility = View.VISIBLE
            setupFrag(ShowYourInviteFrag.newInstance(this@InviteAFriendActivity));
            ivHideShowId.animate().rotation(180F).start();
            rlHideShowId.isEnabled = false
            flag = true
        }

        invite_friend_back_btn.setOnClickListener { finish() }


        ivHideShowId.setOnClickListener {
            if (flag == true) {
                setupFrag(HideYourInviteFrag.newInstance(this@InviteAFriendActivity));
                ivHideShowId.animate().rotation(360F).start();
                tvShowHideInvitesid.setText("Show your invites")
                rlHideShowId.isEnabled = true
                //rlHideShowId.setBackgroundResource(R.color.white)
                view9.visibility = View.GONE
                flag = false
            } else {
                view9.visibility = View.VISIBLE
                // rlHideShowId.setBackgroundResource(R.drawable.invites_card)
                setupFrag(ShowYourInviteFrag.newInstance(this@InviteAFriendActivity));
                ivHideShowId.animate().rotation(180F).start();
                rlHideShowId.isEnabled = false
                tvShowHideInvitesid.setText("Hide your invites")
                flag = true
            }

        }

    }

    //==Fragment setup Funtion=========//
    private fun setupFrag(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flReferFriendId, fragment, "dogDetails")
            .commit()
    }

}
