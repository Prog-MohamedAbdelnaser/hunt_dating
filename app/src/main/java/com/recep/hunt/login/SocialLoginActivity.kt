package com.recep.hunt.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.adapters.SocialLoginChatReceivedAdapter
import com.recep.hunt.adapters.SocialLoginChatSentAdapter
import com.recep.hunt.constants.Constants
import com.recep.hunt.models.LoginChatMessageModel
import com.recep.hunt.setupProfile.SetupProfileActivity
import com.recep.hunt.setupProfile.SetupProfileGalleryActivity
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_social_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class SocialLoginActivity : AppCompatActivity(),View.OnClickListener {


    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText :EditText
    private val adapter = GroupAdapter<ViewHolder>() // using groupie to render recyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)

        init()
    }
    private fun init(){
        recyclerView = find(R.id.social_login_recyclerView)
        messageEditText = find(R.id.type_msg_et)
        setupRecyclerView()
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.send_msg_btn -> {
                    if(messageEditText.text.isNotEmpty()){
                        addMessage()
                    }
                }
                R.id.connect_with_fb_btn -> launchActivity<SetupProfileActivity>()
                R.id.connect_with_google_btn -> launchActivity<SetupProfileGalleryActivity>()
                R.id.connect_with_insta_btn -> toast("Insta")
            }
        }
    }

    private fun setupRecyclerView(){
        val linearLayout = LinearLayoutManager(this@SocialLoginActivity)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter
        val chatData = dummyChatdata()
        for(data in chatData){
            //msg type case
            if(data.type == Constants.messageSentType){
                adapter.add(SocialLoginChatSentAdapter(this@SocialLoginActivity,data))
            }else{
                adapter.add(SocialLoginChatReceivedAdapter(this@SocialLoginActivity,data))
            }
        }
    }

    private fun addMessage(){
        val message = messageEditText.text.toString()
        val model = LoginChatMessageModel(message,Constants.messageSentType)
        adapter.add(SocialLoginChatSentAdapter(this@SocialLoginActivity,model))
        adapter.notifyDataSetChanged()
        messageEditText.setText("")
        this@SocialLoginActivity.hideKeyboard()


    }

    //Dummy Chat Data
    private fun dummyChatdata():ArrayList<LoginChatMessageModel>{
        val data = ArrayList<LoginChatMessageModel>()
        if(data.size == 0){
            data.add(LoginChatMessageModel("You might like to become a member of our loyalty program and accumulate points.",Constants.messageSentType))
            data.add(LoginChatMessageModel("Interesting",Constants.messageReceivedType))
            data.add(LoginChatMessageModel("i am looking forward to it ",Constants.messageReceivedType))
            data.add(LoginChatMessageModel("Sure i will call you asap!",Constants.messageSentType))
        }
        return data
    }


}
