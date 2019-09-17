package com.recep.hunt.login.instagramDetail

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object Utils {

    @Throws(IOException::class)
    fun streamToString(`is`: InputStream?): String {
        var str = ""

        if (`is` != null) {
            val sb = StringBuilder()
            var line: String? = null

            try {
                val reader = BufferedReader(InputStreamReader(`is`))
                while ((line!! in reader.readLine()) != null) {
                    sb.append(line)
                }

                reader.close()
            } finally {
                `is`.close()
            }

            str = sb.toString()
        }

        return str
    }
}
