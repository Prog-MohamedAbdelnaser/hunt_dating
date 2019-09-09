package com.recep.hunt.profile.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Created by Rishabh Shukla
 * on 2019-08-30
 * Email : rishabh1450@gmail.com
 */

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getUser():LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)




}