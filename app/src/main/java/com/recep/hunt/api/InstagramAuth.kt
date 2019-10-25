package com.recep.hunt.api

import com.recep.hunt.constants.Constants
import com.recep.hunt.volleyHelper.APIController
import com.recep.hunt.volleyHelper.ServiceVolley


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class InstagramAuth {
    companion object{
        fun makeInstagramAuthentication(){
            val params = Constants.instaGramUrl
            val path = "client_id=50861e5c85324044a5c2b888735d7ca8&redirect_uri=https://www.gethunt.app/&response_type=code"

            val serviceInjection = ServiceVolley()
            val apiController = APIController(serviceInjection)

            apiController.get(path,params){

            }
        }
    }
}