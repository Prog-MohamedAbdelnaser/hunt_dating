package com.recep.hunt.contactUs

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import kotlinx.android.synthetic.main.on_board_adapter.view.*
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList

class FAQsPayQuestAnsActivty : BaseActivity(), ContactUsInterface {
    override fun itemSelected(title: Int) {

    }

    private lateinit var vpFaqQesAnsId: ViewPager
    private lateinit var tbLayoutFaqQesAnsId: TabLayout
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs_pay_qest_ans)
        setScreenTitle(R.string.faqs_pay)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { finish() }
        init()
    }

    private fun init() {
        vpFaqQesAnsId = find(R.id.vpFaqQesAnsId)
        tbLayoutFaqQesAnsId = find(R.id.tbLayoutFaqQesAnsId)
        setupViewPager()
    }


    private fun setupViewPager() {
        val titleArray = arrayListOf(R.string.wanna_go_hunting, R.string.wanna_go_hunting1, R.string.wanna_go_hunting2)
        val subtitleArray = arrayListOf(R.string.be_part_of_hunt, R.string.be_part_of_hunt1, R.string.be_part_of_hunt2)
        vpFaqQesAnsId.adapter = FaqPayQestPagerAdapter(this, subtitleArray)
        tbLayoutFaqQesAnsId.setupWithViewPager(vpFaqQesAnsId)

        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == 3) {
                currentPage = 0
            }
            vpFaqQesAnsId.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }


    class FaqPayQestPagerAdapter(private val context: Context, private val subtitle: ArrayList<Int>) : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val item = LayoutInflater.from(container.context).inflate(R.layout.faq_qes_answer_adapter, container, false)
           // item.on_board_adapter_subtitle_view.text = context.getString(subtitle[position])
            container.addView(item)
            return item
        }


        override fun getCount(): Int {
            return 5
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
