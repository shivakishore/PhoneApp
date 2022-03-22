package com.shiva.phoneapp

/**
 * Created by Shiva Kishore on 2/23/2022.
 */
import android.app.Application
import android.content.Context

class PhoneApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
    companion object {
        lateinit  var appContext: Context
    }
}