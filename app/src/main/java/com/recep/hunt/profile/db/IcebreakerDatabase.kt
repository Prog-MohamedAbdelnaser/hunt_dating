package com.recep.hunt.profile.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.recep.hunt.profile.model.IceBreakerModel
import com.recep.hunt.profile.model.IcebreakerDao
import java.util.*


/**
 * Created by Rishabh Shukla
 * on 2019-09-06
 * Email : rishabh1450@gmail.com
 */

@Database(entities = [IceBreakerModel::class],version = 1)
abstract class IcebreakerDatabase : RoomDatabase() {

    abstract fun icebreakerDao():IcebreakerDao

    companion object{
        private var instance : IcebreakerDatabase? = null

        fun getInstance(context: Context):IcebreakerDatabase?{
            if(instance == null){
                synchronized(IcebreakerDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        IcebreakerDatabase::class.java,"ice_breaker_questions"
                    ).fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }


        private val roomCallback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance).execute()

            }
        }
    }



    class PopulateDbAsyncTask(db:IcebreakerDatabase?):AsyncTask<Unit,Unit,Unit>(){
        private val icebreakerDao = db?.icebreakerDao()

        override fun doInBackground(vararg params: Unit?) {
            icebreakerDao?.insertQuestion(IceBreakerModel("What you expect from relationship?",Date().toString(),"Friendship","Hookup","Benifits"))
        }
    }

}