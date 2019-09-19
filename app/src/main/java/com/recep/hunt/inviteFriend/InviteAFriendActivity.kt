package com.recep.hunt.inviteFriend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_invite_afriend.*

class InviteAFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_afriend)
        init()
    }
    private fun init(){
        send_invite_btn.setOnClickListener {
            launchActivity<MyInviteCreditActivity>()
        }
    }
}
