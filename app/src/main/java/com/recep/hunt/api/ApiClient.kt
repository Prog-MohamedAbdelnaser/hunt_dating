package com.recep.hunt.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    var BASE_URL: String = "https://www.gethunt.app"

    val builder: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(builder)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val getClient: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }


    val retrofitRx: Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(builder)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val getClientRx: ApiInterface by lazy {
        retrofitRx.create(ApiInterface::class.java)
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