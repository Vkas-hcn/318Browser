package com.spring.breeze.proud.horse.fast.vjiropa.verv

import androidx.annotation.Keep

@Keep
data class WkvrnBean(
    val title: String,
    val url: String,
    val date: String = "",
    var selected: Boolean = false,
    var isGone: Boolean = false
)