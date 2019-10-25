package com.recep.hunt.volleyHelper

import org.json.JSONObject

/**
 * Created by RishabhShukla on 11/02/19.
 */
class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection
    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)
    }
    override fun get(path: String, params: String, completionHandler: (response: String?) -> Unit) {
        service.get(path, params, completionHandler)
    }
}