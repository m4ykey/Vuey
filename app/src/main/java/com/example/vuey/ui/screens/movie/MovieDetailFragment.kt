package com.example.vuey.ui.screens.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.example.vuey.data.database.model.MovieEntity
import com.example.vuey.data.models.movie.detail.Cast
import com.example.vuey.data.models.movie.detail.Genre
import com.example.vuey.data.models.movie.detail.SpokenLanguage
import com.example.vuey.databinding.FragmentMovieDetailBinding
import com.example.vuey.ui.adapter.CastAdapter
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()
    private val arguments: MovieDetailFragmentArgs by navArgs()
    private lateinit var castAdapter: CastAdapter

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
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

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

            val handler = Handler()
            handler.postDelayed({
                progressBar.visibility = View.GONE
            }, 500)

            val databaseArguments = arguments.movieEntity

            val genres: List<MovieEntity.GenreEntity> = databaseArguments.genre
            val genresList = genres.joinToString(separator = ", ") { it.name }

            val spokenLanguage: List<MovieEntity.SpokenLanguageEntity> =
                databaseArguments.spokenLanguage
            val languageList = spokenLanguage.joinToString(separator = ", ") { it.name }

            if (languageList.isEmpty()) {
                txtSpokenLanguages.text = getString(R.string.languages_empty)
            } else {
                txtSpokenLanguages.text = languageList
            }

            txtMovieTitle.text = databaseArguments.title
            txtInfo.text =
                "${databaseArguments.runtime} • $genresList • ${databaseArguments.release_date}"
            txtVoteAverage.text = databaseArguments.vote_average
            txtVoteCount.text = " | " + databaseArguments.vote_count

            txtOverview.text = databaseArguments.overview
            txtOverviewFull.text = databaseArguments.overview
            linearLayoutOverview.setOnClickListener {
                if (txtOverviewFull.visibility == View.GONE) {
                    txtOverview.visibility = View.GONE
                    txtOverviewFull.visibility = View.VISIBLE
                } else {
                    txtOverview.visibility = View.VISIBLE
                    txtOverviewFull.visibility = View.GONE
                }
            }

            if (!databaseArguments.backdrop_path.isNullOrEmpty()) {
                imgBackdrop.load(TMDB_IMAGE_ORIGINAL + databaseArguments.backdrop_path) {
                    crossfade(500)
                    crossfade(true)
                }
            } else {
                imgBackdrop.load(TMDB_IMAGE_ORIGINAL + databaseArguments.poster_path) {
                    crossfade(500)
                    crossfade(true)
                }
            }

            Log.i("MovieDetailId", "onViewCreated: ${databaseArguments.id}")
            viewModel.getCast(databaseArguments.id)
            val castList = databaseArguments.cast.map { cast ->
                Cast(
                    id = cast.id,
                    character = cast.character,
                    name = cast.name,
                    profile_path = cast.profile_path.toString()
                )
            }
            Log.i("MovieDetailCast", "onViewCreated: $castList")
            castAdapter.submitCast(castList)

            Log.i("MovieDetailDatabaseInfo", "onViewCreated: $databaseArguments")
        }

        viewModel.movieDetail(arguments.searchMovie.id)
        viewModel.movieDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    with(binding) {

                        hideLoading()

                        val movieDetail = response.data

                        val movieYear = sdf.parse(movieDetail!!.release_date)
                        val formattedDate = outputDateFormat.format(movieYear!!)

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

                        val voteAverage = movieDetail.vote_average
                        val formattedVoteAverage = String.format("%.1f", voteAverage)

                        imgSave.setOnClickListener {
                            val cast = viewModel.movieCredits.value?.data?.cast?.map {
                                MovieEntity.CastEntity(
                                    character = it.character,
                                    profile_path = it.profile_path,
                                    id = it.id,
                                    name = it.name,
                                    movieId = arguments.searchMovie.id
                                )
                            }
                            val saveMovieToDatabase = MovieEntity(
                                id = arguments.searchMovie.id,
                                overview = arguments.searchMovie.overview,
                                poster_path = movieDetail.poster_path,
                                backdrop_path = movieDetail.backdrop_path,
                                release_date = formattedDate,
                                runtime = formattedRuntime,
                                title = movieDetail.title,
                                vote_average = formattedVoteAverage,
                                vote_count = movieDetail.vote_count,
                                genre = movieDetail.genres.map { genre ->
                                    MovieEntity.GenreEntity(
                                        name = genre.name
                                    )
                                },
                                spokenLanguage = movieDetail.spoken_languages.map { language ->
                                    MovieEntity.SpokenLanguageEntity(
                                        name = language.name
                                    )
                                },
                                cast = cast!!
                            )
                            viewModel.insertCast(cast)
                            viewModel.insertMovie(saveMovieToDatabase)
                            Log.i(
                                "MovieDetailSaveToDatabase",
                                "onViewCreated: $saveMovieToDatabase + $cast"
                            )
                        }

                        Log.e("MovieDetail", "Error: ${viewModel.movieDetail.value?.message}")
                        Log.i("MovieDetail", "Movie ID: ${movieDetail.id}")

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
                        txtVoteAverage.text = formattedVoteAverage
                        txtVoteCount.text = " | " + movieDetail.vote_count.toString()
                        txtMovieTitle.text = movieDetail.title
                        txtInfo.text = "$formattedRuntime • $genresList • $formattedDate"
                        if (languagesList.isEmpty()) {
                            txtSpokenLanguages.text = getString(R.string.languages_empty)
                        } else {
                            txtSpokenLanguages.text = languagesList
                        }

                        txtOverview.text = arguments.searchMovie.overview
                        txtOverviewFull.text = arguments.searchMovie.overview
                        linearLayoutOverview.setOnClickListener {
                            if (txtOverviewFull.visibility == View.GONE) {
                                txtOverview.visibility = View.GONE
                                txtOverviewFull.visibility = View.VISIBLE
                            } else {
                                txtOverview.visibility = View.VISIBLE
                                txtOverviewFull.visibility = View.GONE
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Movie Detail Error")
                        .setMessage("${response.message}")
                        .show()
                    hideLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.movieCredit(arguments.searchMovie.id)
        viewModel.movieCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    val movieCredits = response.data

                    if (movieCredits!!.cast.isEmpty()) {
                        binding.recyclerViewTopCast.visibility = View.GONE
                        binding.txtEmptyCast.visibility = View.VISIBLE
                    }
                    movieCredits.let { castEntries ->
                        castAdapter.submitCast(castEntries.cast)
                    }
                }

                is Resource.Failure -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Cast Error")
                        .setMessage("${response.message}")
                        .show()
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