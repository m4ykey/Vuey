package com.example.vuey.feature_movie.presentation

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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentDetailMovieBinding
import com.example.vuey.feature_movie.presentation.adapter.CastAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val args: DetailMovieFragmentArgs by navArgs()

    private val viewModel: MovieViewModel by viewModels()

    private val castAdapter by lazy { CastAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMovieDetail(args.movie.id)
        viewModel.getMovieCast(args.movie.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovieDetail()
        observeMovieCast()

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            recyclerViewTopCast.apply {
                adapter = castAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }

    }

    private fun observeMovieCast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieCastUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.VISIBLE
                        }
                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.isError.toString(),
                                Snackbar.LENGTH_LONG
                            )
                        }
                        uiState.castMovieData.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            binding.progressBarCast.visibility = View.GONE
                            castAdapter.submitCast(uiState.castMovieData)
                        }
                    }
                }
            }
        }
    }

    private fun observeMovieDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(
                                requireView(),
                                uiState.isError.toString(),
                                Snackbar.LENGTH_LONG
                            )
                        }

                        uiState.detailMovieData != null -> {
                            binding.progressBar.visibility = View.GONE

                            val movieDetail = uiState.detailMovieData

                            val genreList =
                                movieDetail.genreList.joinToString(separator = ", ") { it.name }
                            val movieHour = movieDetail.runtime / 60
                            val movieMinute = movieDetail.runtime % 60
                            val movieRuntime = if (movieHour == 0) {
                                String.format("%d min", movieMinute)
                            } else {
                                String.format(
                                    "%d ${getString(R.string.hour)} %d min",
                                    movieHour,
                                    movieMinute
                                )
                            }

                            with(binding) {

                                if (movieDetail.spokenLanguages.isEmpty()) {
                                    txtSpokenLanguages.text = getString(R.string.languages_empty)
                                }
                                linearLayoutOverview.setOnClickListener {
                                    if (txtOverviewFull.visibility == View.GONE) {
                                        txtOverview.visibility = View.GONE
                                        txtOverviewFull.visibility = View.VISIBLE
                                    } else {
                                        txtOverview.visibility = View.VISIBLE
                                        txtOverviewFull.visibility = View.GONE
                                    }
                                }
                                val imagePath = movieDetail.backdropPath.ifEmpty {
                                    movieDetail.posterPath
                                }
                                imgBackdrop.load(TMDB_IMAGE_ORIGINAL + imagePath) {
                                    crossfade(true)
                                    crossfade(1000)
                                }

                                txtMovieTitle.text = movieDetail.title
                                txtOverview.text = movieDetail.overview
                                txtOverviewFull.text = movieDetail.overview
                                txtInfo.text = "$movieRuntime • $genreList • ${
                                    DateUtils.formatAirDate(movieDetail.releaseDate)}"
                                txtSpokenLanguages.text =
                                    movieDetail.spokenLanguages.joinToString(separator = ", ") { it.name }
                                txtVoteAverage.text = movieDetail.voteAverage.formatVoteAverage()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}