package com.example.bilim

import android.content.Context
import com.example.bilim.common.Constants
import com.example.bilim.common.dataSourse.SharedPrefDataSource
import com.example.bilim.common.dataSourse.SharedPrefDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val applicationModule: Module = module {
    single {
        val context = androidApplication()
        context.getSharedPreferences(Constants.USER_NAME_SHARED_PREF, Context.MODE_PRIVATE)
    }

    factory<SharedPrefDataSource> {
        SharedPrefDataSourceImpl(
            sharedPreferences = get()
        )
    }
}