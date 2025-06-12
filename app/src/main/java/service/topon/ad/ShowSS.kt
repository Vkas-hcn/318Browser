package service.topon.ad

import android.content.Context
import android.content.Intent
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



    fun ssFc(context: Context) {
        try {
            Log.e("TAG", "startForegroundService: 1", )
            ContextCompat.startForegroundService(
                context,
                Intent(context, ZFznS::class.java)
            )
        } catch (e: Exception) {
            Log.e("TAG","Error starting startForegroundService: ${e.message}")
            e.printStackTrace()
        }
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