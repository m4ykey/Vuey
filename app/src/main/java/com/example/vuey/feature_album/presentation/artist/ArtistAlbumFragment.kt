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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getArtistDetail(args.artistId)
        viewModel.getArtistInfo(args.artistName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeArtistDetail()
        setupToolbar()
        hideBottomNavigation()

        with(binding) {
            chipFollow.setOnClickListener {
                isArtistFollowed = !isArtistFollowed
                if (isArtistFollowed) {
                    chipFollow.text = "Followed"
                } else {
                    chipFollow.text = "Follow"
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
                viewModel.albumArtistUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                        }

                        uiState.isError?.isNotEmpty() == true -> {
                            showSnackbar(
                                requireView(),
                                uiState.isError.toString(),
                                Snackbar.LENGTH_LONG
                            )
                        }

                        uiState.artistAlbumData != null -> {

                            val artistDetail = uiState.artistAlbumData
                            val artistImage =
                                artistDetail.images.find { it.height == 640 && it.width == 640 }

                            with(binding) {
                                imgArtist.load(artistImage?.url)
                                collapsingToolbar.title = artistDetail.name
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