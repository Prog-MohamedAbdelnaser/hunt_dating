package com.recep.hunt.profile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.model.UserDao


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

@Database(entities = [User::class],version = 1)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao():UserDao
    companion object{
        private var instance : UserDatabase? = null

        fun getInstance(context: Context):UserDatabase?{
            if(instance == null){
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java,"user")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}