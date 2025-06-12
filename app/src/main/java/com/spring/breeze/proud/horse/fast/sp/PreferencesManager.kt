package com.spring.breeze.proud.horse.fast.sp

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("bro_prefs", Context.MODE_PRIVATE)

    var userjson: String by sharedPreferences.string("")
    var startSS: Boolean by sharedPreferences.boolean(false)
}

fun SharedPreferences.string(defaultValue: String): ReadWriteProperty<Any?, String> =
    object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            getString(property.name, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            edit().putString(property.name, value).apply()
        }
    }

fun SharedPreferences.int(defaultValue: Int): ReadWriteProperty<Any?, Int> =
    object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            getInt(property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            edit().putInt(property.name, value).apply()
        }
    }

fun SharedPreferences.boolean(defaultValue: Boolean): ReadWriteProperty<Any?, Boolean> =
    object : ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            getBoolean(property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            edit().putBoolean(property.name, value).apply()
        }
    }
