package com.example.bilim.common.dataSourse

import android.content.SharedPreferences

private const val VALUE = ""

class SharedPrefDataSourceImpl
    (private val sharedPreferences: SharedPreferences) : SharedPrefDataSource {

    override fun getValue(key: String): String = sharedPreferences.getString(key, "Welcome to Bilim") ?: VALUE

    override fun saveValue(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}