package com.recep.hunt.contactUs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.contactUs.model.ChatModel
import com.recep.hunt.contactUs.model.ChatwithusModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.Helpers
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_with_us_chat.*
import kotlinx.android.synthetic.main.chat_main_received_item.view.*
import kotlinx.android.synthetic.main.chat_main_sent_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File



class ChatWithUsChatActivity : Activity() {

    private lateinit var chatSegueModel : ChatwithusModel
    private lateinit var recyclerView : RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_us_chat)

        chatSegueModel = intent.getParcelableExtra(ChatWithUsActivity.chatItemModelKey)
        init()

          ivUploadImage.setOnClickListener {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false).start()
        }

        onGifClick.onClick {
            val mimeTypes = arrayOf("image/gif")
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
                .setType("image/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            startActivityForResult(pickIntent,2)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        val imageFile: File
        if (requestCode === Config.RC_PICK_IMAGES && resultCode === Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            if (images.size == 1) {
                imageFile = File(images[0].path)
                MediaScannerConnection.scanFile(
                    this, arrayOf(imageFile.getAbsolutePath()), null
                ) { path, uri ->
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).start(this)
                }

            }
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                val data=ChatModel(msgType = 3, bitmapImage = bitmap, type = Constants.messageSentType)
                adapter.add(ChatReceivedAdapter(this,data))
                adapter.notifyDataSetChanged()
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        if (requestCode === 2) {
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = data!!.data

                val data=ChatModel(msgType = 2,uriGif = resultUri, type = Constants.messageSentType)
                adapter.add(ChatSentAdapter(this,data))
                adapter.notifyDataSetChanged()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)


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

    //Dummy Chat DataaaAaaa
    private fun dummyChatdata():ArrayList<ChatModel>{
        val data = ArrayList<ChatModel>()
        if(data.size == 0){
            data.add(ChatModel("You might like to become a member of our loyalty program and accumulate points.",1,
                type = Constants.messageReceivedType))
            data.add(ChatModel("Interesting",1, type =  Constants.messageSentType))
            data.add(ChatModel("i am looking forward to it ",1, type = Constants.messageSentType))
            data.add(ChatModel("Sure i will call you asap!",1, type = Constants.messageReceivedType))
        }
        return data
    }
}
class ChatSentAdapter(private val context: Context, private val chatModel:ChatModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.chat_main_sent_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if(chatModel.msgType==1)
        {
            viewHolder.itemView.chat_main_sent_message_item.visibility = View.VISIBLE
            viewHolder.itemView.chat_main_sent_message_item.text = chatModel.msgData
            viewHolder.itemView.lytCrdlayoutSent.visibility=View.GONE


        }

        else if(chatModel.msgType==2)
        {
            viewHolder.itemView.chat_main_sent_message_item.visibility = View.GONE
            viewHolder.itemView.lytCrdlayoutSent.visibility=View.VISIBLE
            viewHolder.itemView.chatImageSent.setImageBitmap(chatModel.bitmapImage)
            Glide.with(context)
                .load(chatModel.uriGif)
            .into( viewHolder.itemView.chatImageSent);
        }
        else{
            viewHolder.itemView.chat_main_sent_message_item.visibility = View.GONE
            viewHolder.itemView.lytCrdlayoutSent.visibility=View.VISIBLE
            viewHolder.itemView.chatImageSent.setImageBitmap(chatModel.bitmapImage)
        }


    }

}
class ChatReceivedAdapter(private val context: Context, private val chatModel:ChatModel):Item<ViewHolder>(){
    override fun getLayout() = R.layout.chat_main_received_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if(chatModel.msgType==1)
        {
            viewHolder.itemView.chat_main_received_message_item.text = chatModel.msgData
            viewHolder.itemView.lytCrdlayoutReceived.visibility=View.GONE


        }
        else if(chatModel.msgType==2)
        {
            viewHolder.itemView.chat_main_sent_message_item.visibility = View.GONE
            viewHolder.itemView.lytCrdlayoutSent.visibility=View.VISIBLE
            viewHolder.itemView.chatImageSent.setImageBitmap(chatModel.bitmapImage)
            Glide.with(context)
                .load(chatModel.uriGif)
                .into( viewHolder.itemView.chatImageRecevied);
        }
        else{
            viewHolder.itemView.chat_main_received_message_item.visibility = View.GONE
            viewHolder.itemView.lytCrdlayoutReceived.visibility=View.VISIBLE
            viewHolder.itemView.chatImageRecevied.setImageBitmap(chatModel.bitmapImage)
        }
    }

}