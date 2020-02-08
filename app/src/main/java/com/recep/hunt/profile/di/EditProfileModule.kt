package com.recep.hunt.profile.di

import com.recep.hunt.data.repositories.EditeProfileRepository
import com.recep.hunt.domain.usecases.EditProfileUseCases
import com.recep.hunt.profile.viewmodel.EditProfileViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val editProfileModule = module {


    factory { EditeProfileRepository(get(),get()) }
    factory { EditProfileUseCases(get()) }
    viewModel { EditProfileViewModel(get()) }
}