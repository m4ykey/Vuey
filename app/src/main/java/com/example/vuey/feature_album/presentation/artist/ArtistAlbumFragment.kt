package com.example.vuey.feature_album.presentation.artist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vuey.R
import com.example.vuey.databinding.FragmentArtistAlbumBinding
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class ArtistAlbumFragment : Fragment() {

    private var _binding: FragmentArtistAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    private val args: ArtistAlbumFragmentArgs by navArgs()

    private var isArtistFollowed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        hideBottomNavigation()

        with(binding) {
            chipFollow.setOnClickListener {
                isArtistFollowed = !isArtistFollowed
                if (isArtistFollowed) {
                    chipFollow.text = getString(R.string.followed)
                } else {
                    chipFollow.text = getString(R.string.follow)
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    private fun setupToolbar() {
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            collapsingToolbar.apply {
                setCollapsedTitleTextAppearance(R.style.collapsingToolbarTitleColor)
                setExpandedTitleTextAppearance(R.style.collapsingToolbarTitleColor)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}