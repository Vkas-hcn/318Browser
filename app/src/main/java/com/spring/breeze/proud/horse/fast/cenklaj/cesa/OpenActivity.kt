package com.spring.breeze.proud.horse.fast.cenklaj.cesa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spring.breeze.proud.horse.fast.vjrwqp.mke.HomeAppActivity
import com.spring.breeze.proud.horse.fast.vjrwqp.mke.MainActivity

class OpenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent = Intent(this, HomeAppActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(mainIntent)
        finish()
    }
}