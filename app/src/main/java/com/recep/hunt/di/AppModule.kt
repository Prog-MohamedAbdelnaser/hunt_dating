package com.rent.client.di

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.nasmanpower.nas.data.sources.resources.AppResources
import com.recep.hunt.api.ApiInterface
import com.recep.hunt.data.repositories.DeviceInfoRepostory
import com.recep.hunt.data.repositories.StringsRepository
import com.rent.client.di.DIConstants.KEY_DEVICE_ID
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val applicationModule = module {

    factory { get<Retrofit>().create(ApiInterface::class.java) }

    single { AppResources(get()) }

    single { StringsRepository(get()) }

    single { DeviceInfoRepostory(androidContext()) }

    factory(KEY_DEVICE_ID) { get<DeviceInfoRepostory>().getDeviceID() }


}