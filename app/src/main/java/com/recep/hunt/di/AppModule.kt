package com.rent.client.di

import android.content.Context
import com.nasmanpower.nas.data.sources.resources.AppResources
import com.recep.hunt.api.ApiInterface
import com.recep.hunt.data.repositories.DeviceInfoRepostory
import com.recep.hunt.data.repositories.StringsRepository
import com.recep.hunt.data.sources.local.AppPreference
import com.recep.hunt.data.sources.local.AppPreferenceConstants
import com.rent.client.di.DIConstants.KEY_DEVICE_ID
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit

val applicationModule = module {

    factory { get<Retrofit>().create(ApiInterface::class.java) }

    single { AppResources(get()) }

    single { StringsRepository(get()) }

    single { DeviceInfoRepostory(androidContext()) }

    factory(KEY_DEVICE_ID) { get<DeviceInfoRepostory>().getDeviceID() }

    single(AppPreferenceConstants.PREFERENCE_FILE_NAME) { androidApplication().getSharedPreferences(
        AppPreferenceConstants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE) }

    single { AppPreference(get(AppPreferenceConstants.PREFERENCE_FILE_NAME)) }

}