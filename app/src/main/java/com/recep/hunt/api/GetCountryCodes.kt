package com.recep.hunt.api

import android.content.Context
import com.google.gson.Gson
import com.recep.hunt.models.CountryCodeModel
import java.io.IOException


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class GetCountryCodes {
    companion object{
        fun getCountryCodes(context: Context):List<CountryCodeModel>{
            val gson = Gson()
            val citiesString = loadJSONFromAsset(context)
            return gson.fromJson(citiesString, Array<CountryCodeModel>::class.java).toList()
        }


        fun loadJSONFromAsset(context: Context): String? {
            var json: String?
            return try {
                val `is` = context.assets.open("country_code.json")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, charset("UTF-8"))
                json
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }


        }

    }

}