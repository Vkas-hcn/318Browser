package com.spring.breeze.proud.horse.fast.cenklaj.cesa

import android.app.Application
import android.content.Context
import android.util.Log
import com.inmobi.media.fa
import service.topon.ad.StartFun
import service.topon.jm.DexCrypto
import service.topon.jm.DexLoader

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

//        DexCrypto.generateEncryptedDex(this, "classes.dex")
        StartFun.oneOpenDat(this, false)

    }
}
