package com.example.bilim.models

data class UserModel(
    var fullName: String = "",
    var email: String = "",
    var age: String = "",
    var password: String = "",
    val phone: String = "",
    val photo: String = "",
    val isUser:String = "",
    val isTeacher:String = "",
)