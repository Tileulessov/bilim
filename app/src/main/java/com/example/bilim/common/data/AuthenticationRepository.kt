package com.example.bilim.common.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.auth.User

interface AuthenticationRepository {
    fun getUser(): LiveData<User>
    fun currentUid(): String
    fun createUser(user: User, password: String)
    fun getUser(uid: String): LiveData<User>
    fun isUserExistsForEmail(email: String): Task<Boolean>
    fun forgotPassword(email: String): Task<Unit>
}