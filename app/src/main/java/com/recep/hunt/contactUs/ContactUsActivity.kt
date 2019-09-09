package com.recep.hunt.contactUs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contact_us_adapter_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.toast

class ContactUsActivity : BaseActivity(),ContactUsInterface {

    override fun itemSelected(title: Int) {
        when(title){
            R.string.faqs -> launchActivity<FAQsActivty>()
            R.string.chat_with_us -> launchActivity<ChatWithUsActivity>()
            else->launchActivity<ContactUsEmailActivity>()
        }
    }
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        setScreenTitle(R.string.contact_us)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()
    }
    private fun init(){
        recyclerView = find(R.id.contact_us_recyclerView)
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        for(item in getData()){
            adapter.add(ContactUsAdapterItem(this,item,true,this))
        }
    }
    private fun getData():ArrayList<ContactUsModel>{
        val data = ArrayList<ContactUsModel>()
        if(data.size == 0){
            data.add(ContactUsModel(R.string.faqs,R.string.faq_detail,R.drawable.faq_icon))
            data.add(ContactUsModel(R.string.chat_with_us,R.string.chat_detail,R.drawable.chat_with_us_icon))
            data.add(ContactUsModel(R.string.email,R.string.email_detail,R.drawable.email_us_icon))
        }

        return data
    }
}
class ContactUsAdapterItem(private val context:Context,private val model:ContactUsModel,private val showArrow:Boolean,private val contactUsInterface:ContactUsInterface):Item<ViewHolder>(){
    override fun getLayout() = R.layout.contact_us_adapter_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.contact_us_item_detail.text = context.resources.getString(model.detail)
        viewHolder.itemView.contact_us_item_title.text = context.resources.getString(model.title)
        viewHolder.itemView.contact_us_item_imageView.image = context.resources.getDrawable(model.image)

        viewHolder.itemView.setOnClickListener {
            contactUsInterface.itemSelected(model.title)
        }

        if(showArrow){
            viewHolder.itemView.imageView9.visibility = View.VISIBLE
        }else{
            viewHolder.itemView.imageView9.visibility = View.GONE
        }



    }
}

interface ContactUsInterface {
    fun itemSelected(title:Int)
}