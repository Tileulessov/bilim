package com.example.bilim

import android.app.Application
import com.example.bilim.course_page.di.coursePageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BilimApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BilimApplication)
            modules(
                applicationModule,
                coursePageModule
            )
        }
    }
}