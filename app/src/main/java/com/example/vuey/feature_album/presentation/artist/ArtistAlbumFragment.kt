package com.example.vuey.feature_album.presentation.artist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentArtistAlbumBinding
import com.example.vuey.feature_album.data.remote.model.spotify.artist.ArtistData
import com.example.vuey.feature_album.presentation.adapter.TopTracksAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class ArtistAlbumFragment : Fragment() {

    private var _binding: FragmentArtistAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    private val args: ArtistAlbumFragmentArgs by navArgs()

    private var isArtistFollowed = false

    private val topTracksAdapter by lazy { TopTracksAdapter() }

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
            getArtistDetail(args.artistId)
            getArtistInfo(args.artistName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeArtistDetail()
        setupToolbar()
        hideBottomNavigation()

        with(binding) {
            recyclerViewTopTracks.adapter = topTracksAdapter
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

    private fun observeArtistDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artistUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {}
                        uiState.isError?.isNotEmpty() == true -> {}
                        uiState.artistData != null -> {

                            val artistInfo = uiState.artistData
                            with(binding) {
                                txtListeners.text = "${artistInfo.stats.listeners} " + getString(R.string.listeners)
                                txtArtistInfo.text = artistInfo.bio.content

                                cardViewInfo.setOnClickListener {
                                    val artistData = ArtistData(
                                        listeners = artistInfo.stats.listeners,
                                        content = artistInfo.bio.content
                                    )
                                    val action = ArtistAlbumFragmentDirections.actionArtistAlbumFragmentToArtistBioFragment(
                                        artistData = artistData
                                    )
                                    findNavController().navigate(action)
                                }
                            }
                        }
                    }
                }
            }
        }
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