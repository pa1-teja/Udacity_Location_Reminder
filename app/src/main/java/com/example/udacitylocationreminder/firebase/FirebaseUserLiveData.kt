package com.example.udacitylocationreminder.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser>() {

    private val firebaseAuthUtils = FirebaseAuthUtils.getFirebaseAuthUtils()
    private val authStateHandler = FirebaseAuth.AuthStateListener { firebaseAuth ->
        FirebaseAuthUtils.setCurrentUser(firebaseAuth.currentUser)
    }
    override fun onActive() {
        firebaseAuthUtils.getFirebaseAuthInstance().addAuthStateListener(authStateHandler)
    }

    override fun onInactive() {
        firebaseAuthUtils.getFirebaseAuthInstance().removeAuthStateListener(authStateHandler)
    }

}