package com.recep.hunt.volleyHelper

import org.json.JSONObject

/**
 * Created by RishabhShukla on 11/02/19.
 */
interface ServiceInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun get(path: String,params:String,completionHandler: (response: String?) -> Unit)
}