package com.recep.hunt.api

import android.text.TextUtils
import android.util.Log
import com.google.gson.GsonBuilder
import com.recep.hunt.application.MyApplication
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.security.cert.CertificateException


object ApiClient {

    var BASE_URL:String="http://165.22.18.129"

    val builder : OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    val retrofit : Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(builder)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val getClient: ApiInterface by  lazy {
        retrofit.create(ApiInterface::class.java)
    }
//        get() {



//            builder.addInterceptor { chain ->
//                val requestBuilder = chain.request().newBuilder()
////                requestBuilder.addHeader(ApiConstant.HEADER_NEWS_API_KEY, ApiConstant.NEWS_KEY)
//                MyApplication.instance?.let {
//                    val token = SharedPrefrenceManager.getUserToken(it)
//                    Log.d("Creator" ,"Token : " + token)
//                    if (!TextUtils.isEmpty(token)) {
//                        requestBuilder.addHeader("Authorization", token)
//                    }
//                }
//                chain.proceed(requestBuilder.build())
//            }

//            return retrofit.create(ApiInterface::class.java)
//
//        }
}