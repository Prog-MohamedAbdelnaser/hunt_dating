package com.recep.hunt.profile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.profile.model.UserBasicInfoQuestionModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_user_profile_edit_question.*
import kotlinx.android.synthetic.main.edit_question_options_item_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image

class UserProfileEditQuestionActivity : AppCompatActivity(),QuestionAnswerSelectedListener {

    override fun onAnswerSelected(ans: Int) {
        val answer = if(questionPosition != Constants.HEIGHT)
            resources.getString(ans)
        else
            ans.toString()

        when(questionPosition){
            Constants.RELATIONSHIP -> SharedPrefrenceManager.setRelationshipStatus(this,answer)
            Constants.HEIGHT -> SharedPrefrenceManager.setUserHeight(this,answer)
            Constants.GYM -> SharedPrefrenceManager.setUserGym(this,answer)
            Constants.EDUCATIONLEVEL -> SharedPrefrenceManager.setUserEducationLevel(this,answer)
            Constants.DRINK -> SharedPrefrenceManager.setUserDrink(this,answer)
            Constants.SMOKE ->SharedPrefrenceManager.setSmoke(this,answer)
            Constants.PETS ->SharedPrefrenceManager.setPets(this,answer)
            Constants.LOOKINGFOR -> SharedPrefrenceManager.setLookingFor(this,answer)
            Constants.KIDS -> SharedPrefrenceManager.setKids(this,answer)
            Constants.ZODIAC->SharedPrefrenceManager.setZodiac(this,answer)
            Constants.RELIGION -> SharedPrefrenceManager.setReligion(this,answer)
        }

        finish()
    }

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var headerImageView : ImageView
    private lateinit var heightEditText: EditText
    private lateinit var questionTextView : TextView
    private var questionPosition = 100
    private var adapter = GroupAdapter<ViewHolder>()
    private lateinit var questionModel : UserBasicInfoQuestionModel
    private var image = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit_question)
        questionModel = intent.getParcelableExtra(UserProfileEditActivity.questionModelKey)
        image = intent.getIntExtra(UserProfileEditActivity.questionImageKey,0)
        questionPosition = intent.getIntExtra(UserProfileEditActivity.questionPositionKey,101)
        init()
    }
    private fun init(){
        toolbar = find(R.id.user_edit_question_toolbar)
        headerImageView = find(R.id.edit_question_header_imageView)
        recyclerView = find(R.id.edit_questions_recyclerView)
        heightEditText = find(R.id.user_profile_edit_question_editText)
        questionTextView = find(R.id.edit_profile_question)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        headerImageView.image = resources.getDrawable(image)
        questionTextView.text = resources.getString(questionModel.question)

        setupQuestionType()

        save_height_btn.setOnClickListener {
            val txt =heightEditText.text.toString()
            if(txt.isNotEmpty()){
                onAnswerSelected(Integer.parseInt(txt))
            }

        }
    }
    private fun setupQuestionType(){
        if(questionModel.isListTypeQuestion){
            heightEditText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            save_height_btn.visibility = View.GONE
            setupRecyclerView()
        }else{
            heightEditText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            save_height_btn.visibility = View.VISIBLE
            val placeHolder = questionModel.optionPlaceholder
            if(placeHolder != null)
            heightEditText.hint = resources.getString(placeHolder)
        }
    }

    private fun setupRecyclerView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val options = questionModel.options

        if(!options.isNullOrEmpty()){
            for (i in 0 until options.size){
                adapter.add(OptionsAdapter(this,options[i],this))
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_questions_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.profile_item_2 -> launchActivity<UserProfileActivity>()
                R.id.filter_item_2->{
                    val bottomSheet = FilterBottomSheetDialog(this)
                    bottomSheet.show(supportFragmentManager,"FilterBottomSheetDialog")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
//Class Edit Question OptionsAdapter : =


class OptionsAdapter(private val context:Context,val option:Int,private val listeners:QuestionAnswerSelectedListener):Item<ViewHolder>(){
    override fun getLayout() = R.layout.edit_question_options_item_layout

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.question_option_text.text = context.resources.getString(option)
        viewHolder.itemView.setOnClickListener {
            listeners.onAnswerSelected(option)
        }

    }
}

interface QuestionAnswerSelectedListener{
    fun onAnswerSelected(ans:Int)
}