package com.example.vuey.feature_movie.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.feature_movie.data.database.entity.MovieEntity
import com.example.vuey.feature_movie.data.api.detail.Cast
import com.example.vuey.feature_movie.data.api.detail.Genre
import com.example.vuey.feature_movie.data.api.detail.SpokenLanguage
import com.example.vuey.databinding.FragmentMovieDetailBinding
import com.example.vuey.feature_movie.data.database.entity.MovieCastEntity
import com.example.vuey.feature_movie.data.database.entity.MovieGenreEntity
import com.example.vuey.feature_movie.data.database.entity.MovieSpokenLanguageEntity
import com.example.vuey.feature_movie.presentation.adapter.CastAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.network.Resource
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()
    private val arguments: MovieDetailFragmentArgs by navArgs()
    private lateinit var castAdapter: CastAdapter
    private var isMovieSaved = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        castAdapter = CastAdapter()

        viewModel.movieCredit(arguments.searchMovie.id)
        viewModel.movieDetail(arguments.searchMovie.id)

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        with(binding) {

            imgBack.setOnClickListener { findNavController().navigateUp() }

            recyclerViewTopCast.apply {
                adapter = castAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
            }, 500)

            val movieArguments = arguments.movieEntity

            val movieEntity = MovieEntity(
                movieBackdropPath = movieArguments.movieBackdropPath,
                movieGenreList = movieArguments.movieGenreList,
                movieCastList = movieArguments.movieCastList,
                id = movieArguments.id,
                movieOverview = movieArguments.movieOverview,
                moviePosterPath = movieArguments.moviePosterPath,
                movieReleaseDate = movieArguments.movieReleaseDate,
                movieRuntime = movieArguments.movieRuntime,
                movieSpokenLanguageList = movieArguments.movieSpokenLanguageList,
                movieTitle = movieArguments.movieTitle,
                movieVoteAverage = movieArguments.movieVoteAverage,
            )

            viewModel.getMovieById(movieArguments.id)
                .observe(viewLifecycleOwner) { movie ->
                    isMovieSaved = if (movie == null) {
                        imgSave.setImageResource(R.drawable.ic_save_outlined)
                        false
                    } else {
                        imgSave.setImageResource(R.drawable.ic_save)
                        true
                    }
                }

            imgSave.setOnClickListener {
                isMovieSaved = !isMovieSaved
                if (isMovieSaved) {
                    showSnackbar(requireView(), getString(R.string.added_to_library))
                    viewModel.insertMovie(movieEntity)
                    imgSave.setImageResource(R.drawable.ic_save)
                } else {
                    showSnackbar(requireView(), getString(R.string.removed_from_library))
                    viewModel.deleteMovie(movieEntity)
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                }
            }

            val castList = movieArguments.movieCastList.map { cast ->
                Cast(
                    character = cast.character,
                    name = cast.name,
                    profile_path = cast.profile_path,
                    id = cast.id
                )
            }
            castAdapter.submitCast(castList)

            val genres: List<MovieGenreEntity> = movieArguments.movieGenreList
            val genresList = genres.joinToString(separator = ", ") { it.name }

            val spokenLanguage: List<MovieSpokenLanguageEntity> =
                movieArguments.movieSpokenLanguageList
            val languageList = spokenLanguage.joinToString(separator = ", ") { it.name }

            if (languageList.isEmpty()) {
                txtSpokenLanguages.text = getString(R.string.languages_empty)
            } else {
                txtSpokenLanguages.text = languageList
            }

            txtMovieTitle.text = movieArguments.movieTitle

            txtInfo.text =
                "${movieArguments.movieRuntime} • $genresList • ${movieArguments.movieReleaseDate}"

            txtVoteAverage.text = movieArguments.movieVoteAverage

            txtOverview.text = movieArguments.movieOverview
            txtOverviewFull.text = movieArguments.movieOverview
            linearLayoutOverview.setOnClickListener {
                if (txtOverviewFull.visibility == View.GONE) {
                    txtOverview.visibility = View.GONE
                    txtOverviewFull.visibility = View.VISIBLE
                } else {
                    txtOverview.visibility = View.VISIBLE
                    txtOverviewFull.visibility = View.GONE
                }
            }

            if (movieArguments.movieBackdropPath.isNotEmpty()) {
                imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieArguments.movieBackdropPath) {
                    crossfade(500)
                    crossfade(true)
                }
            } else {
                imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieArguments.moviePosterPath) {
                    crossfade(500)
                    crossfade(true)
                }
            }
        }

        viewModel.movieCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    val movieCredits = response.data!!

                    if (movieCredits.cast.isEmpty()) {
                        binding.recyclerViewTopCast.visibility = View.GONE
                        binding.txtEmptyCast.visibility = View.VISIBLE
                    }
                    movieCredits.let { castEntries ->
                        castAdapter.submitCast(castEntries.cast)
                    }
                }

                is Resource.Failure -> {
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
                    hideLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.movieDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    with(binding) {

                        hideLoading()

                        val movieDetail = response.data!!

                        viewModel.getMovieById(arguments.movieEntity.id)
                            .observe(viewLifecycleOwner) { movie ->
                                isMovieSaved = if (movie == null) {
                                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                                    false
                                } else {
                                    imgSave.setImageResource(R.drawable.ic_save)
                                    true
                                }
                            }

                        val genres: List<Genre> = movieDetail.genres
                        val genresList = genres.joinToString(separator = ", ") { it.name }

                        val spokenLanguage: List<SpokenLanguage> = movieDetail.spoken_languages
                        val languagesList =
                            spokenLanguage.joinToString(separator = ", ") { it.name }

                        val movieRuntime = movieDetail.runtime
                        val hour = movieRuntime / 60
                        val minute = movieRuntime % 60
                        val formattedRuntime = if (hour == 0) {
                            "${minute}min"
                        } else {
                            "${hour}h ${minute}min"
                        }

                        if (!movieDetail.backdrop_path.isNullOrEmpty()) {
                            imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieDetail.backdrop_path) {
                                crossfade(true)
                                crossfade(500)
                            }
                        } else {
                            imgBackdrop.load(TMDB_IMAGE_ORIGINAL + movieDetail.poster_path) {
                                crossfade(true)
                                crossfade(500)
                            }
                        }
                        txtVoteAverage.text = movieDetail.vote_average.formatVoteAverage()
                        txtMovieTitle.text = movieDetail.title

                        txtInfo.text = "$formattedRuntime • $genresList • ${DateUtils.formatAirDate(movieDetail.release_date)}"

                        if (languagesList.isEmpty()) {
                            txtSpokenLanguages.text = getString(R.string.languages_empty)
                        } else {
                            txtSpokenLanguages.text = languagesList
                        }

                        val movieOverview = movieDetail.overview.ifEmpty {
                            arguments.searchMovie.overview
                        }
                        txtOverview.text = movieOverview
                        txtOverviewFull.text = movieOverview

                        linearLayoutOverview.setOnClickListener {
                            if (txtOverviewFull.visibility == View.GONE) {
                                txtOverview.visibility = View.GONE
                                txtOverviewFull.visibility = View.VISIBLE
                            } else {
                                txtOverview.visibility = View.VISIBLE
                                txtOverviewFull.visibility = View.GONE
                            }
                        }

                        val castEntity =
                            viewModel.movieCredits.value?.data?.cast?.map { cast ->
                                MovieCastEntity(
                                    character = cast.character,
                                    profile_path = cast.profile_path,
                                    name = cast.name,
                                    movieId = movieDetail.id,
                                    id = cast.id
                                )
                            }

                        val movieEntity = MovieEntity(
                            id = movieDetail.id,
                            movieOverview = movieOverview,
                            moviePosterPath = movieDetail.poster_path.toString(),
                            movieBackdropPath = movieDetail.backdrop_path.toString(),
                            movieReleaseDate = DateUtils.formatAirDate(movieDetail.release_date).toString(),
                            movieRuntime = formattedRuntime,
                            movieTitle = movieDetail.title,
                            movieVoteAverage = movieDetail.vote_average.formatVoteAverage(),
                            movieGenreList = movieDetail.genres.map { genre ->
                                MovieGenreEntity(
                                    name = genre.name
                                )
                            },
                            movieSpokenLanguageList = movieDetail.spoken_languages.map { language ->
                                MovieSpokenLanguageEntity(
                                    name = language.name
                                )
                            },
                            movieCastList = castEntity!!
                        )

                        imgSave.setOnClickListener {
                            isMovieSaved = !isMovieSaved
                            if (isMovieSaved) {
                                showSnackbar(requireView(), getString(R.string.added_to_library))
                                viewModel.insertMovie(movieEntity)
                                imgSave.setImageResource(R.drawable.ic_save)
                            } else {
                                showSnackbar(requireView(), getString(R.string.removed_from_library))
                                viewModel.deleteMovie(movieEntity)
                                imgSave.setImageResource(R.drawable.ic_save_outlined)
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
                    hideLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}