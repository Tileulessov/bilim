package com.example.bilim.registration.data

import android.util.Log
import android.widget.Toast
import com.example.bilim.R
import com.example.bilim.models.UserModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterRepositoryImpl : RegisterRepository {

    private val fAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun createUser(user: UserModel, password: String) {
        fAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener(object:OnSuccessListener<AuthResult>{
            override fun onSuccess(p0: AuthResult?) {
                val fUser: FirebaseUser = fAuth.currentUser!!
                fUser.sendEmailVerification()
            }
        })
    }

    override fun isUserExistsForEmail(email: String): Task<Boolean> =
        fAuth.fetchSignInMethodsForEmail(email).onSuccessTask {
            val signInMethods = it?.signInMethods ?: emptyList<String>()
            Tasks.forResult(signInMethods.isNotEmpty())
        }
}