package com.during.festival.rain.falls.one.appbean


import androidx.annotation.Keep
import com.during.festival.rain.falls.one.utils.DataStoreDelegate
import com.during.festival.rain.falls.one.main.IntBorApp.mainStart

@Keep
class OkSpBean {
    var firstPoint: Boolean by DataStoreDelegate(mainStart, "firstPoint", false)
    var adOrgPoint: Boolean by DataStoreDelegate(mainStart, "adOrgPoint", false)
    var getlimit: Boolean by DataStoreDelegate(mainStart, "getlimit", false)
    var fcmState: Boolean by DataStoreDelegate(mainStart, "fcmState", false)
    var admindata: String by DataStoreDelegate(mainStart, "admindata", "")
    var refdata: String by DataStoreDelegate(mainStart, "refdata", "")
    var appiddata: String by DataStoreDelegate(mainStart, "appiddata", "")
    var IS_INT_JSON: String by DataStoreDelegate(mainStart, "IS_INT_JSON", "")
    var isAdFailCount: Int by DataStoreDelegate(mainStart, "isAdFailCount", 0)
    var adFailPost: Boolean by DataStoreDelegate(mainStart, "adFailPost", false)


    var adHourShowNum: Int by DataStoreDelegate(mainStart, "adHourShowNum", 0)
    var adHourShowDate: String by DataStoreDelegate(mainStart, "adHourShowDate", "")
    var adDayShowNum: Int by DataStoreDelegate(mainStart, "adDayShowNum", 0)
    var adDayShowDate: String by DataStoreDelegate(mainStart, "adDayShowDate", "")

    var adClickNum: Int by DataStoreDelegate(mainStart, "adClickNum", 0)


    var h5HourShowNum: Int by DataStoreDelegate(mainStart, "h5HourShowNumKey", 0)
    var h5HourShowDate: String by DataStoreDelegate(mainStart, "h5HourShowDateKey", "")
    var h5DayShowNum: Int by DataStoreDelegate(mainStart, "h5DayShowNumKey", 0)
    var h5DayShowDate: String by DataStoreDelegate(mainStart, "h5DayShowDateKey", "")
}