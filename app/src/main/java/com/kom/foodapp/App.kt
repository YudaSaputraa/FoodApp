package com.kom.foodapp

import android.app.Application
import com.kom.foodapp.data.source.local.database.AppDatabase

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}