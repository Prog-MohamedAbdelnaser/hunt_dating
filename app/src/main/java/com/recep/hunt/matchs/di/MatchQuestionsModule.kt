package com.recep.hunt.matchs.di

import com.recep.hunt.api.ApiInterface
import com.recep.hunt.data.repositories.BeginHuntRepositories
import com.recep.hunt.domain.usecases.BeginHuntLocationUseCases
import com.recep.hunt.matchs.vm.MatchQuestionsViewModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit

var matchQuestionsModule= module {

    factory { BeginHuntRepositories(get(), SharedPrefrenceManager.getUserToken(androidContext())) }
    factory { BeginHuntLocationUseCases(get()) }
    viewModel { MatchQuestionsViewModel(get()) }
}