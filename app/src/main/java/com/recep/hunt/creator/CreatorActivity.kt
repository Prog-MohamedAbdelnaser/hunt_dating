package com.recep.hunt.creator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.creator.adapter.CreatorQuestionAdapter
import com.recep.hunt.creator.model.CreatorQustion
import com.recep.hunt.creator.model.OptionModel
import com.recep.hunt.payment.adapter.FaqPaymentAdapter
import org.jetbrains.anko.find
import android.view.WindowManager
import android.view.Window
import org.aviran.cookiebar2.CookieBar.dismiss
import android.R.id.text2
import android.R.attr.text
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.recep.hunt.profile.model.IceBreakerModel
import kotlinx.android.synthetic.main.activity_creator.*
import kotlinx.android.synthetic.main.add_new_ice_breaker_question_dialog.*
import java.util.*
import kotlin.collections.ArrayList


class CreatorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progrssbar:ProgressBar
    private lateinit var remanigTime:TextView
    private  lateinit var btnHaveAnIde: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)
        init()

    }

    private fun init() {
        recyclerView = find(R.id.question_recyclerView)
        remanigTime=find(R.id.tvProgress)
        progrssbar=find(R.id.ProgressBar)
        btnHaveAnIde=find(R.id.btnDontHaveAnIdea)

        btnHaveAnIde.setOnClickListener {
            askQuestion()
        }

        var progress=0
        var time=4

        val handler = Handler()





        Thread(Runnable {
            while (progress < 100) {

                progress += 25
                time -= 1

                try {
                    Thread.sleep(1500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                handler.post(Runnable {
                    progrssbar.progress=progress
                    remanigTime.text=time.toString()


                    if (progress === 100) {
                        progrssbar.visibility= View.GONE
                        remanigTime.visibility=View.GONE
                        btnHaveAnIde.visibility=View.VISIBLE
                    }
                })
            }
        }).start()


        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = CreatorQuestionAdapter(this, getQustion())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);


        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)





    }

    private fun getQustion(): ArrayList<CreatorQustion>? {

        val list=ArrayList<CreatorQustion>()
        val option=ArrayList<OptionModel>()

        option.add(OptionModel(1,"More People"))

        option.add(OptionModel(2,"Better Performance "))

        option.add(OptionModel(3,"New Features"))


        list.add(CreatorQustion(1,"Be a creator of this app!\n" +
                "Tell us your suggestions!",R.drawable.ic_claps,option))


        return list

    }

    private fun askQuestion(){
        val cancelButton : Button
        val askButton : Button
        val  getIm : EditText
        val option1Et : EditText
        val option2Et  : EditText
        val option3Et  : EditText

        val ll =  LayoutInflater.from(this).inflate(R.layout.ask_question_dailog, null)
        val dialog = Dialog(this)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        cancelButton = dialog.find(R.id.ic_breaker_cancel_btn)
        askButton= dialog.find(R.id.ic_breaker_add_btn)


        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        askButton.setOnClickListener {
            giveUsRate()

                dialog.dismiss()
            }


        dialog.show()
    }

    private fun giveUsRate(){
        val submit : LinearLayout


        val ll =  LayoutInflater.from(this).inflate(R.layout.rate_us_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        submit = dialog.find(R.id.lytGiveRate)


        submit.setOnClickListener {

            dialog.dismiss()
        }


        dialog.show()
    }

}
