package com.recep.hunt.notifications

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.notifications_adapter_item.view.*
import org.jetbrains.anko.find

class NotificationsActivity : BaseActivity() {

    private lateinit var noNotificationsLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setScreenTitle(R.string.notifications)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().setOnClickListener { clearAllNotifications() }
        getBaseCancelBtn().text = "Clear All"
        getBaseCancelBtn().textSize = 13f
        init()
    }

    private fun init() {
        noNotificationsLayout = find(R.id.no_notifications_layout)
        recyclerView = find(R.id.notifications_recyclerView)
        getNotification()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        noNotificationsLayout.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)

        val notificationsViewModel =
            NotificationsViewModel.getNotifications(this@NotificationsActivity)
        for (notification in notificationsViewModel) {
            adapter.add(NotificationsAdapterItem(this, notification))
        }

        if (adapter.itemCount > 0) {
            noNotificationsLayout.visibility = View.GONE
        } else {
            noNotificationsLayout.visibility = View.VISIBLE
        }


    }

    private fun clearAllNotifications() {
        recyclerView.visibility = View.GONE
        noNotificationsLayout.visibility = View.VISIBLE

    }

    private fun getNotification() {


    }
}

class NotificationsAdapterItem(
    private val context: Context,
    private val model: NotificationsModel
) : Item<ViewHolder>() {

    override fun getLayout() = R.layout.notifications_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {

        Glide.with(context).load(model.userImage).into(viewHolder.itemView.notifi_userImage)
        viewHolder.itemView.notifi_userName.text = model.userName
        viewHolder.itemView.notification_time.text = model.notificationTime
        viewHolder.itemView.notifi_userNotification.text = model.notification
    }
}
