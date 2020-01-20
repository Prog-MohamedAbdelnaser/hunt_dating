package com.rent.client.di

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.nasmanpower.nas.data.sources.resources.AppResources
import com.recep.hunt.data.repositories.StringsRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val applicationModule = module {

    single { AppResources(get()) }

    single { StringsRepository(get()) }

}