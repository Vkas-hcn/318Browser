package bigo.ad.zces

import android.app.Service
import android.content.Intent

abstract class ServiceStrategy {
    abstract fun execute(service: Service, intent: Intent?, flags: Int, startId: Int): Int
}