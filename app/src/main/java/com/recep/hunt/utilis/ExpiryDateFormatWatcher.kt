package com.recep.hunt.utilis

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher


/**
 * Created by Rishabh Shukla
 * on 2019-09-07
 * Email : rishabh1450@gmail.com
 */

class ExpiryDateFormatWatcher : TextWatcher {
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.isNotEmpty() && s.length % 3 == 0) {
            val c = s[s.length - 1]
            if (separator == c) {
                s.delete(s.length - 1, s.length)
            }
        }
        if (s.isNotEmpty() && s.length % 3 == 0) {
            val c = s[s.length - 1]
            if (Character.isDigit(c) && TextUtils.split(s.toString(), separator.toString()).size <= 2) {
                s.insert(s.length - 1, separator.toString())
            }
        }
    }
    companion object {
        const val separator = '/'
    }
}