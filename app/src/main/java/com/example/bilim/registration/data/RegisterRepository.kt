package com.example.bilim.registration.data

import com.example.bilim.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.auth.User

interface RegisterRepository {
    fun createUser(user: UserModel, password: String)
    fun isUserExistsForEmail(email: String): Task<Boolean>
}