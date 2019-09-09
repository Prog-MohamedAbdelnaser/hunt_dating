package com.recep.hunt.profile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.model.UserDAO


/**
 * Created by Rishabh Shukla
 * on 2019-08-30
 * Email : rishabh1450@gmail.com
 */

@Database(entities = [User::class],version = 1)
abstract class UserDatabase :RoomDatabase(){
    abstract fun userDao(): UserDAO

    companion object{
        private var instance : UserDatabase? = null

        fun getInstance(context: Context):UserDatabase?{
            if(instance == null){
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"user")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }

//        private val roomCallBack = object : RoomDatabase.Callback(){
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//            }
//        }
    }
}