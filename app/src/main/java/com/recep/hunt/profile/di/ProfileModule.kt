package com.recep.hunt.profile.di

import com.recep.hunt.data.repositories.EditeProfileRepository
import com.recep.hunt.data.repositories.ProfileRepository
import com.recep.hunt.domain.usecases.EditProfileUseCases
import com.recep.hunt.profile.viewmodel.EditProfileViewModel
import com.recep.hunt.profile.viewmodel.ProfileViewModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val profileModule = module {


    factory { ProfileRepository(get(),get()) }
    viewModel { ProfileViewModel(get(),SharedPrefrenceManager.getUserToken(androidContext())) }
}