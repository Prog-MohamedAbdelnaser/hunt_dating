package com.recep.hunt.matchs.di

import com.recep.hunt.matchs.vm.MatchQuestionsViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

var matchQuestionsModule= module {
    viewModel { MatchQuestionsViewModel() }
}