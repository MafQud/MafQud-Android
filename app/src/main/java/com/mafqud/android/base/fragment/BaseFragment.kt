package com.mafqud.android.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mafqud.android.home.HomeActivity


open class BaseFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showHomeBar() {
        val activity = requireActivity()
        if (activity is HomeActivity) {
            activity.homeBarVisibility(isVisible = true)
        }
    }

}