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
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import com.example.vuey.feature_movie.presentation.MovieFragmentDirections
import com.example.vuey.feature_movie.presentation.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.toMovieEntity
import com.example.vuey.util.utils.toSearchMovie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = emptyList<Any>()

    fun submitMovies(newData: List<Any>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: LayoutMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Any) {
            with(binding) {
                when(movie) {
                    is SearchMovie -> {
                        txtMovieTitle.text = movie.title
                        txtDescription.text = movie.overview
                        txtVoteAverage.text = movie.vote_average.formatVoteAverage()

                        if (movie.poster_path != null) {
                            imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.poster_path) {
                                crossfade(true)
                                crossfade(1000)
                            }
                        } else {
                            imgMovie.setImageResource(R.drawable.ic_movie_error) // to change
                        }
                        if (movie.release_date != null && !movie.release_date.isNullOrEmpty()) {
                            txtReleaseDate.text = DateUtils.formatAirDate(movie.release_date)
                        } else {
                            txtReleaseDate.visibility = View.GONE
                        }
                        layoutMovie.setOnClickListener {
                            val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(
                                movieEntity = movie.toMovieEntity(),
                                searchMovie = movie
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    is MovieEntity -> {
                        txtMovieTitle.text = movie.movieTitle
                        txtDescription.text = movie.movieOverview
                        txtReleaseDate.text = movie.movieReleaseDate
                        txtVoteAverage.text = movie.movieVoteAverage

                        if (movie.moviePosterPath != null) {
                            imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.moviePosterPath) {
                                crossfade(true)
                                crossfade(1000)
                            }
                        } else {
                            imgMovie.setImageResource(R.drawable.ic_movie_error) // to change
                        }
                        if (!movie.movieReleaseDate.isNullOrEmpty() && movie.movieReleaseDate != null) {
                            txtReleaseDate.text = DateUtils.formatAirDate(movie.movieReleaseDate)
                        } else {
                            txtReleaseDate.visibility = View.GONE
                        }
                        layoutMovie.setOnClickListener {
                            val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                               movieEntity = movie,
                               searchMovie = movie.toSearchMovie()
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    else -> {}
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
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}