package com.recep.hunt.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.recep.hunt.profile.db.UserDatabase
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by Rishabh Shukla
 * on 2019-08-30
 * Email : rishabh1450@gmail.com
 */

class UserViewModel(application: Application) : AndroidViewModel(application){

    private var repository : UserRepository = UserRepository(application)
    private var user : LiveData<User> = repository.getUser()

    fun insert(user:User){
        repository.insert(user)
    }

    fun getUser() = user
}