package com.recep.hunt.profile.repo

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.recep.hunt.profile.db.UserDatabase
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.model.UserDao


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

class UserRepository(application:Application) {

    private var userDao : UserDao
    private var user:LiveData<User>
    init {
        val database : UserDatabase = UserDatabase.getInstance(application.applicationContext)!!
        userDao = database.userDao()
        user = userDao.getUser()
    }

    fun insertUser(user:User){
        InsertUser(userDao).execute(user)
    }
    fun getUser() = userDao.getUser()

    private class InsertUser(dao:UserDao):AsyncTask<User,Unit,Unit>(){
        val dao = dao
        override fun doInBackground(vararg params: User?) {
            dao.insertUser(params[0]!!)
        }
    }
}