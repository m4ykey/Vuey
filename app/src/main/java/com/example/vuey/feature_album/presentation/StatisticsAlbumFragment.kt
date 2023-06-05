package com.example.vuey.feature_album.presentation

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentStatisticsAlbumBinding
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.math.max

@AndroidEntryPoint
class StatisticsAlbumFragment : Fragment() {

    private var _binding : FragmentStatisticsAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()

        with(binding) {
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                coroutineScope {

                    val startValue = 0

                    val albumsEndValue = viewModel.getAlbumCount().firstOrNull() ?: 0
                    val tracksEndValue  = viewModel.getTotalTracks().firstOrNull() ?: 0

                    val valueAnimator = ValueAnimator.ofInt(startValue, max(albumsEndValue, tracksEndValue))

                    valueAnimator.duration = 1500
                    valueAnimator.interpolator = AccelerateDecelerateInterpolator()

                    valueAnimator.addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Int
                        txtAlbumsNumber.text = if (animatedValue <= albumsEndValue) animatedValue.toString() else albumsEndValue.toString()
                        txtSongs.text = if (animatedValue <= tracksEndValue) animatedValue.toString() else tracksEndValue.toString()
                    }

                    valueAnimator.start()
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}