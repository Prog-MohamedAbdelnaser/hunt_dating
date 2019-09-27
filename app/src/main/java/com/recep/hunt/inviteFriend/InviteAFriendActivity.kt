package com.recep.hunt.inviteFriend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.recep.hunt.R
import com.recep.hunt.inviteFriend.InviteFragment.HideYourInviteFrag
import com.recep.hunt.inviteFriend.InviteFragment.ShowYourInviteFrag
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_invite_afriend.*

class InviteAFriendActivity : AppCompatActivity() {

    var flag: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_afriend)
        init()

    }

    private fun init() {
        //default fragemnt setup
        setupFrag(HideYourInviteFrag.newInstance(this@InviteAFriendActivity));

        send_invite_btn.setOnClickListener {
            launchActivity<MyInviteCreditActivity>()
        }
        invite_friend_back_btn.setOnClickListener { finish() }


        ivHideShowId.setOnClickListener {
            if (flag == true) {
                setupFrag(HideYourInviteFrag.newInstance(this@InviteAFriendActivity));
                ivHideShowId.animate().rotation(360F).start();
                flag = false
            } else {
                setupFrag(ShowYourInviteFrag.newInstance(this@InviteAFriendActivity));
                ivHideShowId.animate().rotation(180F).start();
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
