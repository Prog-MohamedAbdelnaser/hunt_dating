package com.recep.hunt.swipe.di

import com.recep.hunt.api.ApiInterface
import com.recep.hunt.data.repositories.PushNotificationSingleUserRepositories
import com.recep.hunt.domain.usecases.PushNotificationSingleUserUseCases
import com.recep.hunt.matchs.vm.MatchQuestionsViewModel
import com.recep.hunt.swipe.vm.SwipeViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit

var swipModule= module {

    factory { PushNotificationSingleUserRepositories(get()) }
    factory { PushNotificationSingleUserUseCases(get()) }
    viewModel { SwipeViewModel(get()) }
}