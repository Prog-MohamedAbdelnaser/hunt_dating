package com.recep.hunt.notifications

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.notification.NotificationResponse
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.notifications_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : BaseActivity() {

    private lateinit var noNotificationsLayout : LinearLayout
    private lateinit var recyclerView : RecyclerView
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
    private fun init(){
        noNotificationsLayout = find(R.id.no_notifications_layout)
        recyclerView = find(R.id.notifications_recyclerView)
        getNotification()
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        noNotificationsLayout.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)

        val notificationsViewModel = NotificationsViewModel.getNotifications(this@NotificationsActivity)
        for(notification in notificationsViewModel){
            adapter.add(NotificationsAdapterItem(this,notification))
        }
    }

    private fun clearAllNotifications(){
        recyclerView.visibility = View.GONE
        noNotificationsLayout.visibility = View.VISIBLE

    }

    private fun getNotification()
    {



    }
}
class NotificationsAdapterItem(private val context:Context,private val model:NotificationsModel):Item<ViewHolder>(){

    override fun getLayout() = R.layout.notifications_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.notifi_userImage.image = context.resources.getDrawable(model.userImage)
        viewHolder.itemView.notifi_userName.text = model.userName
        viewHolder.itemView.notification_time.text = model.notificationTime
        viewHolder.itemView.notifi_userNotification.text = model.notification
    }
}
