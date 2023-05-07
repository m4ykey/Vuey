package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.feature_movie.data.database.entity.MovieEntity
import com.example.vuey.feature_movie.data.api.search.SearchMovie
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.presentation.MovieFragmentDirections
import com.example.vuey.feature_movie.presentation.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.views.DateUtils
import com.example.vuey.util.views.DiffUtils
import com.example.vuey.util.views.formatVoteAverage
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter(
    private val isFromApi: Boolean
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movieResult = listOf<SearchMovie>()
    private var movieEntityResult = listOf<MovieEntity>()

    fun submitMovie(newMovie: List<SearchMovie>) {
        val oldMovie = DiffUtils(movieResult, newMovie)
        val result = DiffUtil.calculateDiff(oldMovie)
        movieResult = newMovie
        result.dispatchUpdatesTo(this)
    }

    fun submitMovieEntity(newMovie: List<MovieEntity>) {
        val oldMovie = DiffUtils(movieEntityResult, newMovie)
        val result = DiffUtil.calculateDiff(oldMovie)
        movieEntityResult = newMovie
        result.dispatchUpdatesTo(this)
    }

    class MovieViewHolder(private val binding: LayoutMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieEntityResult: MovieEntity) {

            with(binding) {

                val searchMovie = SearchMovie(
                    backdrop_path = movieEntityResult.movieBackdropPath,
                    id = movieEntityResult.id,
                    overview = movieEntityResult.movieOverview,
                    poster_path = movieEntityResult.moviePosterPath,
                    release_date = movieEntityResult.movieReleaseDate,
                    title = movieEntityResult.movieTitle,
                    vote_average = movieEntityResult.movieVoteAverage.replace(",", ".").toDouble(),
                )

                txtMovieTitle.text = movieEntityResult.movieTitle
                txtDescription.text = movieEntityResult.movieOverview
                txtReleaseDate.text = movieEntityResult.movieReleaseDate
                txtVoteAverage.text = movieEntityResult.movieVoteAverage

                if (movieEntityResult.moviePosterPath != null) {
                    imgMovie.load(TMDB_IMAGE + movieEntityResult.moviePosterPath) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgMovie.setImageResource(R.drawable.ic_movie_error)
                }

                if (!movieEntityResult.movieReleaseDate.isNullOrEmpty() && movieEntityResult.movieReleaseDate != null) {
                    txtReleaseDate.text = DateUtils.formatAirDate(movieEntityResult.movieReleaseDate)
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                layoutMovie.setOnClickListener {
                    val action =
                        MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                            searchMovie = searchMovie,
                            movieEntity = movieEntityResult
                        )
                    it.findNavController().navigate(action)
                }
            }
        }

        fun bind(movieResult: SearchMovie) {
            with(binding) {

                if (movieResult.release_date != null && !movieResult.release_date.isNullOrEmpty()) {
                    txtReleaseDate.text = DateUtils.formatAirDate(movieResult.release_date)
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                txtMovieTitle.text = movieResult.title
                txtDescription.text = movieResult.overview
                txtVoteAverage.text = movieResult.vote_average.formatVoteAverage()

                if (movieResult.poster_path != null) {
                    imgMovie.load(TMDB_IMAGE + movieResult.poster_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgMovie.setImageResource(R.drawable.ic_movie_error)
                }

                val movieEntity = MovieEntity(
                    movieBackdropPath = movieResult.backdrop_path.toString(),
                    id = movieResult.id,
                    movieOverview = movieResult.overview,
                    moviePosterPath = movieResult.poster_path.toString(),
                    movieReleaseDate = DateUtils.formatAirDate(movieResult.release_date).toString(),
                    movieRuntime = "",
                    movieTitle = movieResult.title,
                    movieVoteAverage = movieResult.vote_average.formatVoteAverage(),
                    movieSpokenLanguageList = emptyList(),
                    movieGenreList = emptyList(),
                    movieCastList = emptyList()
                )

                layoutMovie.setOnClickListener {
                    val action =
                        SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(
                            searchMovie = movieResult,
                            movieEntity = movieEntity
                        )
                    it.findNavController().navigate(action)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = LayoutMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (isFromApi) {
            holder.bind(movieResult[position])
        } else {
            holder.bind(movieEntityResult[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isFromApi) {
            movieResult.size
        } else {
            movieEntityResult.size
        }
    }
}