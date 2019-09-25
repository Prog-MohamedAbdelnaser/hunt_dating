package com.recep.hunt.profile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.contactUs.ContactUsActivity
import com.recep.hunt.inviteFriend.InviteAFriendActivity
import com.recep.hunt.payment.PaymentMethodActivity
import com.recep.hunt.payment.SelectPaymentMethodsActivity
import com.recep.hunt.premium.HuntPremiumActivity
import com.recep.hunt.premium.MyCardsActivity
import com.recep.hunt.profile.listeners.UserProfileSettingListeners
import com.recep.hunt.profile.viewmodel.IcebreakerViewModel
import com.recep.hunt.profile.viewmodel.UserViewModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.recep.hunt.utilis.setMargins
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.delete_account_logout_btn_layout.view.*
import kotlinx.android.synthetic.main.delete_account_reason_dialog_layout.*
import kotlinx.android.synthetic.main.dont_want_to_join_hunt_dialog.*
import kotlinx.android.synthetic.main.minimal_header_title_item.view.*
import kotlinx.android.synthetic.main.notification_title_item_layout.view.*
import kotlinx.android.synthetic.main.number_changed_success_layout.*
import kotlinx.android.synthetic.main.select_plan_header_item_layout.view.*
import kotlinx.android.synthetic.main.social_switch_item_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor

class UserProfileSettingsActivity : BaseActivity(), UserProfileSettingListeners {

    override fun itemSelected(title: String) {
        when (title) {
            resources.getString(R.string.phone_number) -> launchActivity<PhoneNumberSettingActivity>()
            resources.getString(R.string.email) -> launchActivity<EmailSettingsActivity>()
            resources.getString(R.string.push_notifications) -> launchActivity<PushNotificationsSettingsActivity>()
            resources.getString(R.string.questions) -> launchActivity<IcebreakerQuestionActivity>()
            resources.getString(R.string.tickets) -> launchActivity<ContactUsActivity>()
            resources.getString(R.string.add_payment_details) -> launchActivity<PaymentMethodActivity>()
            resources.getString(R.string.invite_a_friend)->launchActivity<InviteAFriendActivity>()

        }
    }

    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    private lateinit var icebreakerViewModel: IcebreakerViewModel
    private var totalQuestionsCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_settings)
        setScreenTitle(R.string.settings)
        getBackButton().setOnClickListener { finish() }
        getBaseCancelBtn().visibility = View.GONE
        init()
    }

    private fun init() {
        icebreakerViewModel = ViewModelProviders.of(this).get(IcebreakerViewModel::class.java)
        recyclerView = find(R.id.settings_recyclerView)
        icebreakerViewModel.getAllQuestions().observe(this, Observer {
            totalQuestionsCount = it.size
            adapter.notifyDataSetChanged()
        })
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        icebreakerViewModel.getAllQuestions().observe(this, Observer {
            totalQuestionsCount = it.size
            adapter.notifyDataSetChanged()
        })


    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.add(SelectPlanHeaderItem(this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.account_settings)))
        val phoneNumber = SharedPrefrenceManager.getUserMobileNumber(this)
        adapter.add(
            DetailItemAdapter(
                resources.getString(R.string.phone_number),
                phoneNumber,
                resources.getString(R.string.verify_phone_to_secure),
                this
            )
        )
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.notifications_setting)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.email), 0, listener = this))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.push_notifications), 0, listener = this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.connected_accounts)))
        adapter.add(SocialSwitchItemAdapter(this, R.drawable.facebook_setting, resources.getString(R.string.fb), true))
        adapter.add(
            SocialSwitchItemAdapter(
                this,
                R.drawable.linked_setting,
                resources.getString(R.string.linked_in),
                true
            )
        )
        adapter.add(
            SocialSwitchItemAdapter(
                this,
                R.drawable.spotify_setting,
                resources.getString(R.string.spotify),
                false
            )
        )
        adapter.add(SocialSwitchItemAdapter(this, R.drawable.inst_setting, resources.getString(R.string.insta), false))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.payment)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.add_payment_details), 0, listener = this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.ice_breaker_questions)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.questions), 0, listener = this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.help_support)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.tickets), 2, listener = this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.other)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.invite_a_friend), 0, listener = this))
        adapter.add(MinimalHeaderItemAdapter(resources.getString(R.string.legal)))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.license), 0, false, listener = this))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.privacy_policy), 0, listener = this))
        adapter.add(NotificationTitleItemAdapter(resources.getString(R.string.terms_conditions), 0, listener = this))
        adapter.add(DeleteAccountAndLogoutItem(this))

    }

}


//Select Plan Header item
class SelectPlanHeaderItem(private val context: Context) : Item<ViewHolder>() {
    private lateinit var oneMonthLayout: LinearLayout
    private lateinit var sixMonthLayout: LinearLayout
    private lateinit var twelveMonthLayout: LinearLayout
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
    private val extendedHeight = 143
    private val extendedWidth = 130
    override fun getLayout() = R.layout.select_plan_header_item_layout

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

        viewHolder.itemView.get_hunt_premium_btn.setOnClickListener {
            context.launchActivity<SelectPaymentMethodsActivity>()
        }
    }

    private val normalMargin = 24
    private val extendedMargin = 16
    private fun setSelection(value:String){
        when(value){
            Constants.oneMonthValue -> {
                oneMonthLayout.setMargins(extendedMargin,0,0,0)

                twelveMonthLayout.setMargins(0,8,normalMargin,8)

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
                oneMonthLayout.setMargins(normalMargin,8,0,8)

                twelveMonthLayout.setMargins(0,8,normalMargin,8)

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
                oneMonthLayout.setMargins(normalMargin,8,0,8)

                twelveMonthLayout.setMargins(0,0,extendedMargin,0)

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

//Minimal Header Item
class MinimalHeaderItemAdapter(private val text: String) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.minimal_header_title_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.minimal_header_item_text.text = text
    }
}

//Detail Item
class DetailItemAdapter(
    private val title: String,
    private val rightDetail: String,
    private val belowDetail: String,
    val listener: UserProfileSettingListeners
) : Item<ViewHolder>() {

    private lateinit var titleTextView: TextView
    private lateinit var rightDetailTextView: TextView
    private lateinit var belowDetailTextView: TextView
    override fun getLayout() = R.layout.detail_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {
        titleTextView = viewHolder.itemView.find(R.id.detail_item_title_text)
        rightDetailTextView = viewHolder.itemView.find(R.id.detail_item_right_detail_text)
        belowDetailTextView = viewHolder.itemView.find(R.id.detail_item_below_detail_text)

        titleTextView.text = title
        rightDetailTextView.text = rightDetail
        belowDetailTextView.text = belowDetail

        viewHolder.itemView.setOnClickListener {
            listener.itemSelected(title)
        }

    }
}

//Notification Title ITem
class NotificationTitleItemAdapter(
    private val title: String,
    private val count: Int,
    private val showArrow: Boolean = true,
    private val listener: UserProfileSettingListeners
) : Item<ViewHolder>() {
    private lateinit var notificationCountBtn: Button
    private lateinit var notificationTitleText: TextView
    override fun getLayout() = R.layout.notification_title_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {

        notificationCountBtn = viewHolder.itemView.find(R.id.notification_item_count_btn)
        notificationTitleText = viewHolder.itemView.find(R.id.notification_item_title_text)

        notificationTitleText.text = title

        if (count != 0) {
            notificationCountBtn.visibility = View.VISIBLE
            notificationCountBtn.text = count.toString()
        } else {
            notificationCountBtn.visibility = View.GONE
        }

        if (showArrow) {
            viewHolder.itemView.arrow_icon.visibility = View.VISIBLE
        } else {
            viewHolder.itemView.arrow_icon.visibility = View.INVISIBLE
        }

        viewHolder.itemView.setOnClickListener {
            listener.itemSelected(title)
        }
    }
}

//Social switch item
class SocialSwitchItemAdapter(
    private val ctx: Context,
    private val socialIcon: Int,
    private val title: String,
    private val switchValue: Boolean
) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.social_switch_item_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.social_switch_title.text = title
        viewHolder.itemView.social_switch_image.image = ctx.resources.getDrawable(socialIcon)
        viewHolder.itemView.social_switch_control.isChecked = switchValue

    }

}

//Delete Account And Logout Item
class DeleteAccountAndLogoutItem(private val ctx: Context) : Item<ViewHolder>() {
    override fun getLayout() = R.layout.delete_account_logout_btn_layout
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.deleteAccountBtn.setOnClickListener()
        {
            deleteAccountDialog()
        }
    }


    private fun deleteAccountDialog() {
        val ll = LayoutInflater.from(ctx).inflate(R.layout.delete_account_first_dailog_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.reason1_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.reason2_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.reason3_btn.setOnClickListener {
            dialog.dismiss()

        }
        dialog.reason4_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.reason5_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.reason6_btn.setOnClickListener {
            dialog.dismiss()
            otherReasonDialog()
        }
        dialog.show()

        dialog.show()

    }

    private fun otherReasonDialog() {
        val ll = LayoutInflater.from(ctx).inflate(R.layout.delete_account_reason_dialog_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.delete_account_back_btn.setOnClickListener { dialog.dismiss() }
        dialog.delete_account_submit_btn.setOnClickListener {
            dialog.dismiss()
            deleteAccountSuccessDialog()

        }

        dialog.show()

    }

    private fun deleteAccountSuccessDialog() {
        val ll = LayoutInflater.from(ctx).inflate(R.layout.number_changed_success_layout, null)
        val dialog = Dialog(ctx)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.number_change_success_dialog_title.text = ctx.resources.getText(R.string.you_have_successfully_deactivated)
        dialog.lottieAnimationView2.playAnimation()
        dialog.ok_btn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

}


