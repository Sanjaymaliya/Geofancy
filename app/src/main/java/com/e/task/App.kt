package com.e.task

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import com.e.task.koin.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application(), Application.ActivityLifecycleCallbacks {

    init {
        instance = this

    }

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val listOfModule = arrayListOf(viewModule)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOfModule)
        }

        registerActivityLifecycleCallbacks(this)

    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        
    }

    override fun onActivityStarted(p0: Activity) {
        
    }

    override fun onActivityResumed(p0: Activity) {
        
    }

    override fun onActivityPaused(p0: Activity) {
        
    }

    override fun onActivityStopped(p0: Activity) {
        
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        
    }

    override fun onActivityDestroyed(p0: Activity) {
        
    }


}
