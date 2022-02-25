package com.example.udacitylocationreminder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.databinding.ActivityMainBinding
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(mainBinding.parentNavHostFragment.id)
        navController = navHostFragment?.findNavController()!!
    }


}