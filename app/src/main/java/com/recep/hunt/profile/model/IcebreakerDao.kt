package com.recep.hunt.profile.model

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Created by Rishabh Shukla
 * on 2019-09-06
 * Email : rishabh1450@gmail.com
 */

@Dao
interface IcebreakerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question:IceBreakerModel)

    @Query("DELETE FROM ice_breaker_questions WHERE id=:ID")
    fun deleteQuestion(ID:Int)

    @Query("SELECT * FROM ice_breaker_questions")
    fun getAllIcebreakerQuestions():LiveData<List<IceBreakerModel>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateIcebreakerQuestion(question:IceBreakerModel)

    @Query("UPDATE ice_breaker_questions SET  question =:newQuestion ,date=:newDate,option1=:newOption1,option2=:newOption2,option3=:newOption3 WHERE id=:ID")
    fun updateQuestion(newQuestion:String,newDate:String,newOption1:String,newOption2:String,newOption3:String,ID:Int)
}