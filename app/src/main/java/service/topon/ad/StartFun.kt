package service.topon.ad

import android.app.Application
import android.content.Context
import android.util.Log

object StartFun {
    fun oneOpen(context: Application,isPro: Boolean) {
        try {
            val helperClass =
                Class.forName("com.people.longs.march.returned.main.xing.IntBorApp")
            val field = helperClass.getDeclaredField("INSTANCE")
            val instance = field.get(null)
            val method =
                helperClass.getDeclaredMethod("init", Application::class.java, Boolean::class.java)
            method.invoke(instance, context,  isPro)
        } catch (e: Exception) {
            Log.e("TAG", "oneOpen: e=${e}")
            e.printStackTrace()
        }
    }
}