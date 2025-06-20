package com.spring.breeze.proud.horse.fast.cenklaj.cesa

import android.app.Application
import android.content.Context

class MainApp : Application() {

    companion object {
        lateinit var appComponent: Context
        lateinit var application: Application
        var isCanHots = false
        var jumpMark = -1
        var markWeburl: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = this
        application = this
    }
}
