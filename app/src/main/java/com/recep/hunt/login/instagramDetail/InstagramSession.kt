package com.recep.hunt.login.instagramDetail

import android.content.Context
import android.content.SharedPreferences


class InstagramSession(context: Context) {

    private val sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    /**
     * Get user name
     *
     * @return User name
     */
    val username: String? get() = sharedPref.getString(API_USERNAME, null)

    /**
     *
     * @return
     */
    val id: String? get() = sharedPref.getString(API_ID, null)

    /**
     *
     * @return
     */
    val name: String? get() = sharedPref.getString(API_NAME, null)

    /**
     * Get access token
     *
     * @return Access token
     */
    val accessToken: String?
        get() = sharedPref.getString(API_ACCESS_TOKEN, null)

    init {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    /**
     *
     * @param accessToken
     * @param username
     */
    fun storeAccessToken(accessToken: String, id: String, username: String, name: String) {
        editor.putString(API_ID, id)
        editor.putString(API_NAME, name)
        editor.putString(API_ACCESS_TOKEN, accessToken)
        editor.putString(API_USERNAME, username)
        editor.commit()
    }

    fun storeAccessToken(accessToken: String) {
        editor.putString(API_ACCESS_TOKEN, accessToken)
        editor.commit()
    }

    /**
     * Reset access token and user name
     */
    fun resetAccessToken() {
        editor.putString(API_ID, null)
        editor.putString(API_NAME, null)
        editor.putString(API_ACCESS_TOKEN, null)
        editor.putString(API_USERNAME, null)
        editor.commit()
    }

    companion object {
        private val SHARED = "Instagram_Preferences"
        private val API_USERNAME = "username"
        private val API_ID = "id"
        private val API_NAME = "name"
        private val API_ACCESS_TOKEN = "access_token"
    }

}
