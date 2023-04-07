package com.example.vuey.util.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.example.vuey.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomNavHostFragment : NavHostFragment() {

    private var bottomNavigationView : BottomNavigationView? = null
    private var TRANSLATIONY = "translationY"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomMenu)
    }

    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        navController.addOnDestinationChangedListener { _, destionation, _ ->
            when(destionation.id) {
                R.id.albumFragment -> showBottomMenu()
                R.id.movieFragment -> showBottomMenu()
                else -> hideBottomMenu()
            }
        }
    }

    private fun showBottomMenu() {
        bottomNavigationView?.visibility = View.VISIBLE
        val slideUp = ObjectAnimator.ofFloat(bottomNavigationView, TRANSLATIONY, bottomNavigationView?.height?.toFloat() ?: 0f, 0f)
        slideUp.duration = 500
        slideUp.start()
    }

    private fun hideBottomMenu() {
        val slideDown = ObjectAnimator.ofFloat(bottomNavigationView, TRANSLATIONY, 0f, bottomNavigationView?.height?.toFloat() ?: 0f)
        slideDown.duration = 500
        slideDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                bottomNavigationView?.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                bottomNavigationView?.visibility = View.VISIBLE
            }
        })
        slideDown.start()
    }

}