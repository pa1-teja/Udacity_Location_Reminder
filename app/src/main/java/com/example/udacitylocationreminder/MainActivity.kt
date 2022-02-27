package com.example.udacitylocationreminder

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.databinding.ActivityMainBinding
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils

class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(mainBinding.parentNavHostFragment.id)
        navController = navHostFragment?.findNavController()!!
        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id.equals(R.id.launchFragment)) {
            if (FirebaseAuthUtils.getCurrentUser() != null) {
                finish()
            }
        }
    }


}