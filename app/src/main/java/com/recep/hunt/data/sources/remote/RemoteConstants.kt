package com.rent.client.data.sources.remote



object RemoteConstants {
    const val BASE_URL: String = "https://www.gethunt.app"
    const val CONNECT_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60
    const val WRITE_TIMEOUT: Long = 60
    fun getBaseURL():String =  BASE_URL

}