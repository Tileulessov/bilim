package com.example.bilim.common.dataSourse

interface SharedPrefDataSource {
    fun getValue(key:String): String?

    fun saveValue(key: String, value:String?)
}