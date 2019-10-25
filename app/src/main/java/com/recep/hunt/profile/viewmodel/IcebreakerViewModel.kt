package com.recep.hunt.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.recep.hunt.profile.model.IceBreakerModel
import com.recep.hunt.profile.repo.IcebreakerRepository


/**
 * Created by Rishabh Shukla
 * on 2019-09-06
 * Email : rishabh1450@gmail.com
 */

class IcebreakerViewModel(application: Application) : AndroidViewModel(application) {

    private var repository : IcebreakerRepository = IcebreakerRepository(application)
    private var allQuestions : LiveData<List<IceBreakerModel>> = repository.getAllQuestions()

    fun insert(iceBreakerModel: IceBreakerModel){
        repository.insert(iceBreakerModel)
    }
    fun getAllQuestions() = allQuestions

    fun deleteQuestion(id:Int){
        repository.deleteQuestion(id)
    }
    fun updateQuestion(question:IceBreakerModel,id:Int){
        repository.updateQuestion(question,id)
    }

//    fun getAllSections(): LiveData<List<IceBreakerModel>> {
//        return allQuestions
//    }

//    fun totalNumberOfQuestions():Int {
//        val totalCount = allQuestions
//        return totalCount.size
//    }
}