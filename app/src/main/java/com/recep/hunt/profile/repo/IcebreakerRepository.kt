package com.recep.hunt.profile.repo

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.recep.hunt.profile.db.IcebreakerDatabase
import com.recep.hunt.profile.model.IceBreakerModel
import com.recep.hunt.profile.model.IcebreakerDao


/**
 * Created by Rishabh Shukla
 * on 2019-09-06
 * Email : rishabh1450@gmail.com
 */

class IcebreakerRepository(application: Application) {

    private var icebreakerDao: IcebreakerDao

    private var allQuestions : LiveData<List<IceBreakerModel>>

    init {
        val database : IcebreakerDatabase = IcebreakerDatabase.getInstance(application.applicationContext)!!
        icebreakerDao = database.icebreakerDao()
        allQuestions = icebreakerDao.getAllIcebreakerQuestions()
    }

    fun insert(qeustion:IceBreakerModel){
        InsertQuestion(icebreakerDao).execute(qeustion)
    }

    fun getAllQuestions()= allQuestions

    fun deleteQuestion(id:Int){
        DeleteQuestion(icebreakerDao).execute(id)
    }

    fun updateQuestion(question:IceBreakerModel,id:Int){
        UpdateQuestion(icebreakerDao,id).execute(question)
    }

    private class InsertQuestion(dao:IcebreakerDao):AsyncTask<IceBreakerModel,Unit,Unit>(){
        val dao = dao
        override fun doInBackground(vararg params: IceBreakerModel?) {
            dao.insertQuestion(params[0]!!)
        }
    }

    private class DeleteQuestion(dao: IcebreakerDao):AsyncTask<Int,Unit,Unit>(){
        val dao = dao
        override fun doInBackground(vararg params: Int?) {
            dao.deleteQuestion(params[0]!!)
        }
    }

    private class UpdateQuestion(dao:IcebreakerDao,id:Int):AsyncTask<IceBreakerModel,Unit,Unit>(){
        val questionDao = dao
        val Id = id
        override fun doInBackground(vararg params: IceBreakerModel?) {
            val model = params[0]!!
//            questionDao.updateIcebreakerQuestion(model)
            questionDao.updateQuestion(model.question,model.date,model.option1,model.option2,model.option3,Id)
        }
    }

}