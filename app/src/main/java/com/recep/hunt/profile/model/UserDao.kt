package com.recep.hunt.profile.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<User>

    @Insert
    fun insertUser(user: User)
}