package bigo.ad.zces

import android.util.Log

object LogManager {
    fun logWithTag(tag: String, message: String) {
        Log.e(tag, message)
    }
}