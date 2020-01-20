package com.rent.client.data.sources.remote.interceptor

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.recep.hunt.data.repositories.StringsRepository
import com.rent.client.data.exceptions.NetworkException
import com.recep.hunt.data.exceptions.APIException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.SocketTimeoutException
import java.nio.charset.Charset


class ErrorHandlerInterceptor(private val stringsRepository: StringsRepository) : Interceptor {

    private val successStatus = "1"
    private val keyStatus = "status"
    private val keyMessage = "message"
    private val keyJson = "json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            if (e is SocketTimeoutException) {
                throw NetworkException(stringsRepository.getSocketTimeoutExceptionMessage())
            } else {
                throw NetworkException(stringsRepository.getNetworkExceptionMessage())
            }
        }
        val body = response.body()!!
        // Only intercept JSON type responses and ignore the rest.
        if (isJsonTypeResponse(body)) {
            var message = ""
            var status = successStatus
            try {
                val source = body.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                val charset = body.contentType()!!.charset(Charset.forName("UTF-8"))!!
                // Clone the existing buffer is they can only read once so we still want to pass the original one to the chain.
                val json = buffer.clone().readString(charset)
                val obj = JsonParser().parse(json)
                // Capture error code an message.
                if (obj is JsonObject && obj.has(keyStatus)) {
                    status = obj.get(keyStatus).asString
                }
                if (obj is JsonObject && obj.has(keyMessage)) {
                    message = obj.get(keyMessage).asString
                }
            } catch (e: Exception) {
                throw e
            }

            // Check if status has an error code then throw and exception so retrofit can trigger the onFailure callback method.
            // Anything above 400 is treated as a server error.
            if (status != successStatus) {
                throw APIException(status, message)
            }
        }

        return response
    }

    private fun isJsonTypeResponse(body: ResponseBody?): Boolean {
        return body?.contentType() != null &&
                body.contentType()!!.subtype() != null &&
                body.contentType()!!.subtype().toLowerCase() == keyJson
    }

}