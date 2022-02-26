package com.example.udacitylocationreminder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.udacitylocationreminder.BaseFragment
import com.example.udacitylocationreminder.R


/**
 * A simple [Fragment] subclass.
 * Use the [AddReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddReminderFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false)
    }
}