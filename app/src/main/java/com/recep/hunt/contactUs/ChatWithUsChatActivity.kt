package com.recep.hunt.contactUs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.contactUs.model.ChatModel
import com.recep.hunt.contactUs.model.ChatwithusModel
import com.recep.hunt.utilis.Helpers
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_with_us_chat.*
import kotlinx.android.synthetic.main.chat_main_received_item.view.*
import kotlinx.android.synthetic.main.chat_main_sent_item.view.*
import org.jetbrains.anko.find

class ChatWithUsChatActivity : AppCompatActivity() {

    private lateinit var chatSegueModel : ChatwithusModel
    private lateinit var recyclerView : RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_us_chat)
        chatSegueModel = intent.getParcelableExtra(ChatWithUsActivity.chatItemModelKey)
        init()
    }
    private fun init(){
        chat_userName.text = chatSegueModel.userName
        chat_backBtn.setOnClickListener { super.onBackPressed() }
        chat_closeBtn.setOnClickListener { finish() }
        recyclerView = find(R.id.chat_recyclerView)
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        val linearLayout = LinearLayoutManager(this)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter
        val chatData = dummyChatdata()
        for(data in chatData){
            //msg type case
            if(data.type == Constants.messageSentType){
                adapter.add(ChatSentAdapter(this,data))
            }else{
                adapter.add(ChatReceivedAdapter(this,data))
            }
        }

        Helpers.runAnimation(recyclerView)
    }

    //Dummy Chat Data
    private fun dummyChatdata():ArrayList<ChatModel>{
        val data = ArrayList<ChatModel>()
        if(data.size == 0){
            data.add(ChatModel("You might like to become a member of our loyalty program and accumulate points.",Constants.messageReceivedType))
            data.add(ChatModel("Interesting", Constants.messageSentType))
            data.add(ChatModel("i am looking forward to it ",Constants.messageSentType))
            data.add(ChatModel("Sure i will call you asap!",Constants.messageReceivedType))
        }
        return data
    }
}
class ChatSentAdapter(private val context: Context, private val chatModel:ChatModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.chat_main_sent_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_main_sent_message_item.text = chatModel.msg
    }

}
class ChatReceivedAdapter(private val context: Context, private val chatModel:ChatModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.chat_main_received_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_main_received_message_item.text = chatModel.msg
    }

}