package com.example.bilim

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val applicationModule: Module = module {
    single {
        val context = androidApplication()
    }
}