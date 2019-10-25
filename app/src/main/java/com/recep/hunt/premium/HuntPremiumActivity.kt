package com.recep.hunt.premium

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.add_new_payment_method_item.view.*
import kotlinx.android.synthetic.main.dont_want_to_join_hunt_dialog.*
import kotlinx.android.synthetic.main.hunt_premium_header.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor

class HuntPremiumActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hunt_premium)
        setScreenTitle(R.string.hunt_premium)
        getBackButton().setOnClickListener {
            showDialog()
        }
        getBaseCancelBtn().setOnClickListener {
            showDialog()
        }

        init()
    }
    private fun init(){
        recyclerView = find(R.id.hunt_premium_recyclerView)
        setupReyclerView()
    }
    private fun setupReyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.add(HuntPreimumHeaderAdapter(this))
        adapter.add(MasterCardItemAdapter(this))
        adapter.add(GooglePayCardItemAdapter(this))
        adapter.add(PaypalCardItemAdapter(this))
        adapter.add(AddNewPaymentMethodAdapter(this))

    }
    private fun showDialog(){
        val ll =  LayoutInflater.from(this).inflate(R.layout.dont_want_to_join_hunt_dialog, null)
        val dialog = Dialog(this@HuntPremiumActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.reason1_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.reason2_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.reason3_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.reason4_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.reason5_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.reason6_btn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

}

//HUNT-PREMIUM Header
class HuntPreimumHeaderAdapter(private val context: Context):Item<ViewHolder>(){
    private lateinit var oneMonthLayout:LinearLayout
    private lateinit var sixMonthLayout:LinearLayout
    private lateinit var twelveMonthLayout:LinearLayout
    private lateinit var exclusiveBtn : Button
    private lateinit var oneMonthText1:TextView
    private lateinit var oneMonthText2:TextView
    private lateinit var oneMonthText3:TextView

    private lateinit var sixMonthText1:TextView
    private lateinit var sixMonthText2:TextView
    private lateinit var sixMonthText3:TextView

    private lateinit var twelveMonthText1:TextView
    private lateinit var twelveMonthText2:TextView
    private lateinit var twelveMonthText3:TextView

    override fun getLayout() = R.layout.hunt_premium_header
    override fun bind(viewHolder: ViewHolder, position: Int) {
        oneMonthLayout = viewHolder.itemView.find(R.id.hunt_premium_1_month_layout)
        sixMonthLayout = viewHolder.itemView.find(R.id.hunt_premium_6_month_layout)
        twelveMonthLayout = viewHolder.itemView.find(R.id.hunt_premium_12_month_layout)
        exclusiveBtn = viewHolder.itemView.find(R.id.exclusive_btn)
        oneMonthText1 = viewHolder.itemView.find(R.id.hunt_premium_1_month_text1)
        oneMonthText2 = viewHolder.itemView.find(R.id.hunt_premium_1_month_text2)
        oneMonthText3 = viewHolder.itemView.find(R.id.hunt_premium_1_month_text3)

        sixMonthText1 = viewHolder.itemView.find(R.id.hunt_premium_6_month_text1)
        sixMonthText2 = viewHolder.itemView.find(R.id.hunt_premium_6_month_text2)
        sixMonthText3 = viewHolder.itemView.find(R.id.hunt_premium_6_month_text3)

        twelveMonthText1= viewHolder.itemView.find(R.id.hunt_premium_12_month_text1)
        twelveMonthText2= viewHolder.itemView.find(R.id.hunt_premium_12_month_text2)
        twelveMonthText3= viewHolder.itemView.find(R.id.hunt_premium_12_month_text3)

        oneMonthLayout.setOnClickListener {
            setSelection(Constants.oneMonthValue)
        }
        sixMonthLayout.setOnClickListener {
            setSelection(Constants.sixMonthValue)
        }
        twelveMonthLayout.setOnClickListener {
            setSelection(Constants.twelveMonthValue)
        }
    }
    private fun setSelection(value:String){
        when(value){
            Constants.oneMonthValue -> {
                sixMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                oneMonthLayout.background = context.resources.getDrawable(R.drawable.six_month_card_bg)
                twelveMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                exclusiveBtn.visibility = View.GONE

                oneMonthText1.textColor = context.resources.getColor(R.color.pink)
                oneMonthText2.textColor = context.resources.getColor(R.color.pink)
                oneMonthText3.textColor = context.resources.getColor(R.color.pink)

                sixMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                sixMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                sixMonthText3.textColor = context.resources.getColor(R.color.app_text_black)

                twelveMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                twelveMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                twelveMonthText3.textColor = context.resources.getColor(R.color.app_text_black)
            }
            Constants.sixMonthValue -> {
                sixMonthLayout.background = context.resources.getDrawable(R.drawable.six_month_card_bg)
                oneMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                twelveMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                exclusiveBtn.visibility = View.VISIBLE

                oneMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                oneMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                oneMonthText3.textColor = context.resources.getColor(R.color.app_text_black)

                sixMonthText1.textColor = context.resources.getColor(R.color.pink)
                sixMonthText2.textColor = context.resources.getColor(R.color.pink)
                sixMonthText3.textColor = context.resources.getColor(R.color.pink)

                twelveMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                twelveMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                twelveMonthText3.textColor = context.resources.getColor(R.color.app_text_black)
            }
            else->{
                sixMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                oneMonthLayout.background = context.resources.getDrawable(R.drawable.other_month_bg)
                twelveMonthLayout.background = context.resources.getDrawable(R.drawable.six_month_card_bg)
                exclusiveBtn.visibility = View.GONE

                oneMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                oneMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                oneMonthText3.textColor = context.resources.getColor(R.color.app_text_black)

                sixMonthText1.textColor = context.resources.getColor(R.color.app_text_black)
                sixMonthText2.textColor = context.resources.getColor(R.color.app_text_black)
                sixMonthText3.textColor = context.resources.getColor(R.color.app_text_black)

                twelveMonthText1.textColor = context.resources.getColor(R.color.pink)
                twelveMonthText2.textColor = context.resources.getColor(R.color.pink)
                twelveMonthText3.textColor = context.resources.getColor(R.color.pink)
            }
        }
    }
}
class MasterCardItemAdapter(private val context: Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.master_card_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
class GooglePayCardItemAdapter(private val context: Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.google_pay_card_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
class PaypalCardItemAdapter(private val context: Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.paypal_card_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}
class AddNewPaymentMethodAdapter(private val context: Context):Item<ViewHolder>(){
    override fun getLayout() = R.layout.add_new_payment_method_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.add_new_payment_method.setOnClickListener {
            context.launchActivity<AddNewPaymentActivity>()
        }
    }
}

