package com.recep.hunt.contactUs

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.contactUs.model.ChatwithusModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.launchActivity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_with_us_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor

class ChatWithUsActivity : BaseActivity(),ChatWithUsListeners {

    override fun chatItemSelected(model: ChatwithusModel) {
        launchActivity<ChatWithUsChatActivity> {
            putExtra(chatItemModelKey,model)
        }
    }
    companion object{
        const val chatItemModelKey = "chatItemModelKey"
    }
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_us)
        getToolbar().setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        getScreenTitle().textColor = Color.WHITE
        setScreenTitle(R.string.chat_with_us)
        getBackButton().setOnClickListener { finish() }
        getBackButton().image = resources.getDrawable(R.drawable.ic_arrow_back_white)
        getBaseCancelBtn().visibility = View.GONE

        init()
    }
    private fun init(){
        recyclerView = find(R.id.chat_with_us_recyclerView)
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        for(item in getData()){
            adapter.add(ChatWithUsAdapterItem(this,item))
        }
    }
    private fun getData():ArrayList<ChatwithusModel>{
        val data = ArrayList<ChatwithusModel>()
        if(data.size == 0){
            data.add(ChatwithusModel(R.drawable.boy_casual_eyes,"Lariza Ertata","We are able to provide any kind of help","1 h"))
            data.add(ChatwithusModel(R.drawable.boy_casual_eyes,"Arone","We are able to provide any kind of help","5 h"))
            data.add(ChatwithusModel(R.drawable.boy_casual_eyes,"Touhid Likhon","We are able to provide any kind of help","3 h"))
            data.add(ChatwithusModel(R.drawable.boy_casual_eyes,"Rishabh Shukla","We are able to provide any kind of help","2 h"))
        }
        return data
    }
}
class ChatWithUsAdapterItem(private val listeners:ChatWithUsListeners,private val model:ChatwithusModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.chat_with_us_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_with_us_item_msg_time.text = "${model.messageTime} ago"
        Picasso.get().load(model.userImage).into(viewHolder.itemView.chat_with_us_item_userImage)
        viewHolder.itemView.chat_with_us_item_userName.text = model.userName
        viewHolder.itemView.chat_with_us_item_userMessage.text = model.userMessage
        viewHolder.itemView.setOnClickListener {
            listeners.chatItemSelected(model)
        }
    }
}
interface ChatWithUsListeners{
    fun chatItemSelected(model:ChatwithusModel)
}

