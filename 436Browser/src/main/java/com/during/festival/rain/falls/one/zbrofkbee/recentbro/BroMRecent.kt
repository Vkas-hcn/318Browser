package com.during.festival.rain.falls.one.zbrofkbee.recentbro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep

@Keep
class BroMRecent: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("Bc")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("Bc") as Intent?
            if (eIntent != null) {
                try {
                    Log.e("TAG", "onReceive: PngTwoMRecent", )
                    context.startActivity(eIntent)
                    return
                } catch (e: Exception) {
                }
            }
        }
    }
}