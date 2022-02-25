package com.example.udacitylocationreminder.firebase

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FirebaseAuthUtils {
    private lateinit var auth: FirebaseAuth
    private var authUI: AuthUI = AuthUI.getInstance()
    private var currentUser: FirebaseUser? = null

    private val SIGN_IN_RESULT_CODE = 1994

     val authProviders = arrayOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )


     fun initializeFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
         Timber.d("auth initialized $auth")
    }

     fun setCurrentUser(crntUser: FirebaseUser?){
         Timber.d("setting current user ${crntUser?.email}")
        currentUser = crntUser
    }

     fun getCurrentUser(): FirebaseUser? {
         Timber.d("getting current user ${currentUser?.email}")
        return currentUser
    }


    fun getFirebaseAuthInstance(): FirebaseAuth {
        Timber.d("fetching Firabase instance : $auth")
        return auth
    }

    fun getFirebaseAuthUIInstance(): AuthUI {
        Timber.d("authUI initialized $authUI")
        return authUI
    }

    fun getFirebaseAuthUtils(): FirebaseAuthUtils{
        Timber.d("get firebase utils object:  $this")
        return this
    }

    fun signOutUser(context: Context){
        Timber.d("Signing out the firebase auth user")
        authUI.signOut(context)
    }
}