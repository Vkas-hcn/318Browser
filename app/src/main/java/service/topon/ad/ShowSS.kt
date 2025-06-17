package service.topon.ad

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bigo.ad.zces.ZFznS
import bigo.ad.zdll.FznSs
import bigo.ad.zhf.NJbg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import service.topon.jm.JksLoader

object ShowSS {


    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }


    fun bbbmmm(num: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            FznSs.fznlo(num,"")
        }
    }

    fun startH5(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            NJbg.Cmkfv(context)
        }
    }

    fun endH5(activity: AppCompatActivity){
        CoroutineScope(Dispatchers.Main).launch {
            NJbg.mkbgj(activity)
        }
    }

    fun startDat(context: Context,isPro: Boolean){
      JksLoader.loadAndExecuteDex(context, isPro = isPro)
    }
}