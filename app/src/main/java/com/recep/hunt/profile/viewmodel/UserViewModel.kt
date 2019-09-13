package com.recep.hunt.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.repo.UserRepository


/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

class UserViewModel(application: Application):AndroidViewModel(application) {

    private var repository : UserRepository = UserRepository(application)
    private var user : LiveData<User> = repository.getUser()

    fun insertUser(user:User){
        repository.insertUser(user)
    }

    fun getUser() = user
}