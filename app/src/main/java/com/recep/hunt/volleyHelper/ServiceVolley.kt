package com.recep.hunt.volleyHelper

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.recep.hunt.constants.Constants
import org.json.JSONObject

/**
 * Created by RishabhShukla on 11/02/19.
 */
class ServiceVolley : ServiceInterface {
    val TAG = ServiceVolley::class.java.simpleName
    val basePath = Constants.BASEURL
    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.POST, basePath + path, params,
                Response.Listener<JSONObject> { response ->
                    Log.e(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun get(path: String, params: String, completionHandler:(response: String?) -> Unit) {
        val url = "$basePath$path?$params"
        val stringRequest = object : StringRequest(Method.GET,url,
                Response.Listener<String> { response ->
                    Log.e(TAG,"/get request URL : $url")
                    Log.e(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                }, Response.ErrorListener { error ->
            VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
            completionHandler(null)

        }){

        }
        BackendVolley.instance?.addToRequestQueue(stringRequest, TAG)
    }

}



