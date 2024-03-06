package com.ketchup.dailymanna.koin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CreateKoin: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(appModule).androidContext(this@CreateKoin)
        }
    }
}