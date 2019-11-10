package com.recep.hunt.api

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.recep.hunt.application.MyApplication
import com.recep.hunt.utilis.SharedPrefrenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLSession
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.security.cert.CertificateException


object ApiClient {

    var BASE_URL:String="http://165.22.18.129"
    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()



            builder.addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
//                requestBuilder.addHeader(ApiConstant.HEADER_NEWS_API_KEY, ApiConstant.NEWS_KEY)
                MyApplication.instance?.let {
                    val token = SharedPrefrenceManager.getApiToken(it)
//                    val token = "eyJpdiI6Ijcydm1kR1kwSHpvczRLTnZEUFdQanc9PSIsInZhbHVlIjoiR0JOR3I0ZG56S2lOY09ESXdWUWdpUT09IiwibWFjIjoiYTI3Y2Y5NDc3ZDhkMDVmOTJkMjMwZWE0ZDQyYzg5OWNiMDExYzUzYWNmNTg4NWJhNTc2ODBkNjQ1MGI0YTAwZiJ9"
                    if (!TextUtils.isEmpty(token)) {
                        requestBuilder.addHeader("Authorization", token)
                    }
                }
                chain.proceed(requestBuilder.build())
            }


            builder.addInterceptor(interceptor)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)

        }



}