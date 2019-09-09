package com.recep.hunt.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.profile.model.IceBreakerModel
import com.recep.hunt.profile.viewmodel.IcebreakerViewModel
import com.recep.hunt.utilis.BaseActivity
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.add_new_ice_breaker_question_dialog.*
import kotlinx.android.synthetic.main.ice_breaker_adapter_item.view.*
import org.jetbrains.anko.find
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class IcebreakerQuestionActivity : BaseActivity(),IceBreakerListeners {


    override fun btnClicked(title: String, position: Int,question: IceBreakerModel) {
        when(title){
            resources.getString(R.string.delete) -> deleteQuestion(position,question.id)
            else -> addEditQuestion(question)
        }
    }

    private fun deleteQuestion(position: Int,id: Int){
        iceBreakerViewModel.deleteQuestion(id)
        adapter.notifyItemRemoved(position)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var addNewQuestionBtn : Button
    private lateinit var iceBreakerViewModel: IcebreakerViewModel
    private lateinit var adapter : IceBreakerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icebreaker_question)
        setScreenTitle(R.string.ice_breaker_questions)
        getBaseCancelBtn().visibility = View.GONE
        getBackButton().setOnClickListener { super.onBackPressed() }
        init()
    }

    private fun init(){
        recyclerView = find(R.id.ice_breaker_question_recyclerView)
        addNewQuestionBtn = find(R.id.add_new_ice_brkr_question_btn)
        iceBreakerViewModel = ViewModelProviders.of(this).get(IcebreakerViewModel::class.java)
        addNewQuestionBtn.setOnClickListener {
            addEditQuestion()
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        adapter = IceBreakerAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))

        iceBreakerViewModel.getAllQuestions().observe(this,
            androidx.lifecycle.Observer {
                adapter.setQuestions(it)

            })

    }
    private fun addEditQuestion(questionModel:IceBreakerModel? = null){
        val cancelButton : Button
        val addButton : Button
        val questionEt : EditText
        val option1Et : EditText
        val option2Et  : EditText
        val option3Et  : EditText

        val ll =  LayoutInflater.from(this).inflate(R.layout.add_new_ice_breaker_question_dialog, null)
        val dialog = Dialog(this@IcebreakerQuestionActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        cancelButton = dialog.find(R.id.ic_breaker_cancel_btn)
        addButton= dialog.find(R.id.ic_breaker_add_btn)
        questionEt = dialog.find(R.id.add_ice_breaker_question_et)
        option1Et = dialog.find(R.id.icebreaker_option_1_et)
        option2Et = dialog.find(R.id.icebreaker_option_2_et)
        option3Et = dialog.find(R.id.icebreaker_option_3_et)

        if(questionModel !=  null){
            questionEt.setText(questionModel.question)
            option1Et.setText(questionModel.option1)
            option2Et.setText(questionModel.option2)
            option3Et.setText(questionModel.option3)
            addButton.text = resources.getString(R.string.edit)
            dialog.add_a_ques_dialog.text = "Edit a question"
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        addButton.setOnClickListener {
            val question = questionEt.text.toString()
            val option1 = option1Et.text.toString()
            val option2 = option2Et.text.toString()
            val option3 = option3Et.text.toString()

            if(question.isNotEmpty() && option1.isNotEmpty() && option2.isNotEmpty() && option3.isNotEmpty()){
                if(questionModel != null){
                    iceBreakerViewModel.updateQuestion(IceBreakerModel(question,Date().toString(),option1,option2,option3),questionModel.id)
                    adapter.notifyDataSetChanged()
                }else{
                    addQuestion(IceBreakerModel(question,Date().toString(),option1,option2,option3))
                }

                dialog.dismiss()
            }

        }
        dialog.show()
    }

    private fun addQuestion(question:IceBreakerModel){
        iceBreakerViewModel.insert(question)
        adapter.notifyDataSetChanged()
    }
}





class IceBreakerAdapter(val listeners: IceBreakerListeners) : RecyclerView.Adapter<IceBreakerAdapter.IcebreakerViewHolder>(){

    private var model : List<IceBreakerModel> = ArrayList()

    fun setQuestions(questions:List<IceBreakerModel>){
        this.model = questions
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IcebreakerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ice_breaker_adapter_item,parent,false))
    override fun getItemCount() = model.size
    override fun onBindViewHolder(holder: IcebreakerViewHolder, position: Int) {
        holder.setupViews(model[position])
        holder.editBtn.setOnClickListener {
            listeners.btnClicked(holder.editBtn.text.toString(),position,model[position])
        }

        holder.deleteBtn.setOnClickListener {
            listeners.btnClicked(holder.deleteBtn.text.toString(),position,model[position])
        }
    }

    class IcebreakerViewHolder(view:View) : RecyclerView.ViewHolder(view){

        val editBtn = view.find<Button>(R.id.ice_breaker_edit_btn)
         val deleteBtn = view.find<Button>(R.id.ice_breaker_delete_btn)
        private val question  =  view.find<TextView>(R.id.ice_breaker_question_txt)
        private val dateTextView =  view.find<TextView>(R.id.ice_breaker_date_time_txt)

        fun setupViews(model:IceBreakerModel){
            question.text = "${model.question}?"
            val date = Date(model.date)
            val format = SimpleDateFormat("dd MMMM,yyyy - h:mm a",Locale.ENGLISH)
            val dateString = format.format(date)
            dateTextView.text = dateString
        }
    }
}
interface IceBreakerListeners {
    fun btnClicked(title:String,position:Int,question:IceBreakerModel)
}