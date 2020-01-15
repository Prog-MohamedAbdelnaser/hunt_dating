package com.recep.hunt.creator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.creator.RecyclerItemClickListenr
import com.recep.hunt.creator.model.CreatorQustion
import org.jetbrains.anko.find

class CreatorQuestionAdapter(val context: Context,val item:ArrayList<CreatorQustion>?):RecyclerView.Adapter<CreatorQuestionAdapter.CreatorQuestionViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorQuestionViewholder {
        return CreatorQuestionViewholder(LayoutInflater.from(parent.context).inflate(R.layout.creator_question_item,parent,false))
    }

    override fun getItemCount() = item!!.size

    override fun onBindViewHolder(holder: CreatorQuestionViewholder, position: Int) {
        if(item != null){
            holder.setupViews(item[position])
        }
        holder.optionRecylerview.addOnItemTouchListener(RecyclerItemClickListenr(context, holder.optionRecylerview, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                var a=position
            }
            override fun onItemLongClick(view: View?, position: Int) {
                var a=position            }
        }))
    }

    inner class CreatorQuestionViewholder(view: View) : RecyclerView.ViewHolder(view){
        var questionIcon:ImageView=view.find(R.id.ivQustionIcon)
        var question : TextView = view.find(R.id.tvQuestion)
        var optionRecylerview : RecyclerView =  view.find(R.id.option_recyclerView)


        lateinit var caretorOption:CreatorOptionAdapter


        fun setupViews(model:CreatorQustion?){

            if(model != null){
                question.text=model.questionText
                questionIcon.setImageDrawable(context.resources.getDrawable(model.icon))
                questionIcon.visibility=View.GONE
                caretorOption=CreatorOptionAdapter(context, model.optionsList)

                optionRecylerview.adapter = caretorOption

                optionRecylerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);

            }



        }
    }
}