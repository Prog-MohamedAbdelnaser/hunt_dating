package com.recep.hunt.profile.repo

import android.app.Application
import android.os.AsyncTask
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.recep.hunt.profile.db.UserDatabase
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.model.UserDAO


/**
 * Created by Rishabh Shukla
 * on 2019-08-30
 * Email : rishabh1450@gmail.com
 */

class UserRepository(application: Application) {
    private var userDAO:UserDAO
    private var user:LiveData<User>
    init {
        val database : UserDatabase = UserDatabase.getInstance(application.applicationContext)!!
        userDAO = database.userDao()
        user = userDAO.getUser()
    }

    fun insert(user: User){
        InsertUser(userDAO).execute(user)
    }
    fun getUser() = user

    private class InsertUser(dao:UserDAO):AsyncTask<User,Unit,Unit>(){
        val userDao = dao
        override fun doInBackground(vararg params: User?) {
            userDao.insertUser(params[0]!!)
        }
    }
}