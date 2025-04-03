package com.during.festival.rain.falls.one.ad


import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.main.IntBorApp.adLimiter

object AdLimiter {
    fun canShowAd(isPostInt: Boolean): Boolean {
        val jsonBean = IntBorApp.getAdminData() ?: return true
        return !adLimiter.canShowAd(jsonBean,isPostInt)
    }
}
