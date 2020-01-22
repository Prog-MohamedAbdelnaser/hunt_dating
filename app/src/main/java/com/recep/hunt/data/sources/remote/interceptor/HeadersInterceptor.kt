package com.rent.client.data.sources.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class HeadersInterceptor(private val token:String) : Interceptor, KoinComponent {

    private val keyAuthorization = "Authorization"
    private val keyApiKey = "ApiKey"
    private val apiKeyValue = ""
    private val keyLanguage = "Language"

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(createNewRequestWithApiKey(chain.request()))

    private fun createNewRequestWithApiKey(oldRequest: Request): Request {

        val requestBuilder = oldRequest.newBuilder()
                //.addHeader(keyApiKey, apiKeyValue)
            .addHeader(keyAuthorization, token)

        return requestBuilder.build()
    }
}