package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.data.api.search.SearchMovie
import com.example.vuey.feature_movie.data.database.entity.MovieEntity
import com.example.vuey.feature_movie.presentation.MovieFragmentDirections
import com.example.vuey.feature_movie.presentation.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.toMovieEntity
import com.example.vuey.util.utils.toSearchMovie

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
        notifyDataSetChanged()
    }

    fun submitMovieEntity(newMovie: List<MovieEntity>) {
        val oldMovie = DiffUtils(movieEntityResult, newMovie)
        val result = DiffUtil.calculateDiff(oldMovie)
        movieEntityResult = newMovie
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class MovieViewHolder(private val binding: LayoutMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieEntityResult: MovieEntity) {

            with(binding) {

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

                if (movieEntityResult.movieReleaseDate.isNullOrEmpty().not() && movieEntityResult.movieReleaseDate != null) {
                    txtReleaseDate.text = DateUtils.formatAirDate(movieEntityResult.movieReleaseDate)
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                layoutMovie.setOnClickListener {
                    val action =
                        MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                            searchMovie = movieEntityResult.toSearchMovie(),
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

                layoutMovie.setOnClickListener {
                    val action =
                        SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(
                            searchMovie = movieResult,
                            movieEntity = movieResult.toMovieEntity()
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