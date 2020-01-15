package com.recep.hunt.utilis

import android.util.Log

class LogUtil {




    companion object {
        private val isDebugModeEnabled = true //todo set it to false before release apk

        /**
         * @param TAG set tag for log
         * @param msg set message to display in log
         */
        fun d(TAG: String, msg: String) {
            var TAG = TAG
            var msg = msg
            if (isDebugModeEnabled) {
                if (TAG == null) {
                    TAG = "TAG NULL FOUND"
                }
                if (msg == null) {
                    msg = "MSG NULL FOUND"
                }
                Log.d(TAG, msg)
            }
        }

        /**
         * @param TAG set tag for log
         * @param msg set message to display in log
         */
        fun e(TAG: String, msg: String) {
            var TAG = TAG
            var msg = msg
            if (isDebugModeEnabled) {
                if (TAG == null) {
                    TAG = "TAG NULL FOUND"
                }
                if (msg == null) {
                    msg = "MSG NULL FOUND"
                }
                Log.e(TAG, msg)
            }
        }

    }

}