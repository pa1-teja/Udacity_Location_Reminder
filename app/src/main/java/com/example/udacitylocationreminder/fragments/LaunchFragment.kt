package com.example.udacitylocationreminder.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.BaseFragment
import com.example.udacitylocationreminder.R
import com.example.udacitylocationreminder.databinding.FragmentLaunchBinding
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import com.example.udacitylocationreminder.viewmodelfactories.LaunchFragmentViewModelFactory
import com.example.udacitylocationreminder.viewmodels.LaunchFragmentViewModel
import com.firebase.ui.auth.AuthUI
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [LaunchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LaunchFragment : BaseFragment(), View.OnClickListener {

    private lateinit var getContent: ActivityResultLauncher<Intent>
    private lateinit var launchBinding: FragmentLaunchBinding

    private val launchViewModel: LaunchFragmentViewModel by lazy {
        val launchFragmentViewModelFactory = LaunchFragmentViewModelFactory(requireContext())
        ViewModelProvider(this,launchFragmentViewModelFactory).get(LaunchFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSignInOrReminderScreen()
        getContent.launch(getSignInIntent())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_launch,container,false)
        launchBinding.lifecycleOwner = this
        launchBinding.loginBtn.setOnClickListener(this)

        launchViewModel.firebaseUser.observe(viewLifecycleOwner, Observer {
            Timber.d(" firebase user name : ${it.email}")
        })

        return launchBinding.root
    }

    override fun onClick(view: View?) {
        when(view?.id){
            launchBinding.loginBtn.id ->{
                getContent.launch(getSignInIntent())
            }
        }
    }


    fun getSignInIntent(): Intent {
        return FirebaseAuthUtils.getFirebaseAuthUIInstance().createSignInIntentBuilder().setAvailableProviders(
            FirebaseAuthUtils.authProviders.toMutableList()
        ).build()
    }

    fun showSignInOrReminderScreen(){
         getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode.equals(Activity.RESULT_OK)){
                val firebaseUser = FirebaseAuthUtils.getCurrentUser()
                if (firebaseUser != null){
                    findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToLocationReminderFragment())
                    Timber.d("response data ${firebaseUser?.email}")
                }
            }
        }

        Timber.d("get Content value $getContent")
    }
}