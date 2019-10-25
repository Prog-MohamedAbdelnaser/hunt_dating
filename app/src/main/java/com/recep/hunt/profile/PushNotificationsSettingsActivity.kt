package com.recep.hunt.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity

class PushNotificationsSettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_notifications_settings)
        setScreenTitle(R.string.push_notificaions)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
    }
}
