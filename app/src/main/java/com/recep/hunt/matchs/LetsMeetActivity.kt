package com.recep.hunt.matchs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.utilis.SharedPrefrenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LetsMeetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lets_meet)
    }

    override fun onPause() {
        super.onPause()
        makeUserOfline()
    }
    fun makeUserOfline()
    {
        val makeUserOnline= MakeUserOnline(false)

        val call = ApiClient.getClient.makeUserOnline(makeUserOnline, SharedPrefrenceManager.getUserToken(this))

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeUserOnlineResponse>,
                response: Response<MakeUserOnlineResponse>
            ) {

            }

        })

    }
}
