package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.feature_movie.presentation.MovieFragmentDirections
import com.example.vuey.feature_movie.presentation.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.toMovie
import com.example.vuey.util.utils.toMovieEntity

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = emptyList<Any>()

    fun submitMovie(newData : List<Any>) {
        val oldData = movies.toList()
        movies = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding : LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie : Any) {
            with(binding) {
                when (movie) {
                    is MovieEntity -> {
                        txtMovieTitle.text = movie.movieTitle
                        txtMovieOverview.text = movie.movieOverview
                        imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.moviePosterPath) {
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAverageVote.text = movie.movieVoteAverage.formatVoteAverage()
                        if (movie.movieReleaseDate.isEmpty()) {
                            txtReleaseDate.visibility = View.GONE
                        } else {
                            txtReleaseDate.text = DateUtils.formatAirDate(movie.movieReleaseDate)
                        }
                        layoutMovie.setOnClickListener {
                            val action = MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(
                                movie = movie.toMovie(),
                                movieEntity = movie
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    is MovieList -> {
                        txtMovieTitle.text = movie.title
                        txtMovieOverview.text = movie.overview
                        imgMovie.load(TMDB_IMAGE_ORIGINAL + movie.poster_path) {
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAverageVote.text = movie.vote_average.formatVoteAverage()
                        if (movie.release_date.isEmpty()) {
                            txtReleaseDate.visibility = View.GONE
                        } else {
                            txtReleaseDate.text = DateUtils.formatAirDate(movie.release_date)
                        }
                        layoutMovie.setOnClickListener {
                            val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToDetailMovieFragment(
                                movie = movie,
                                movieEntity = movie.toMovieEntity()
                            )
                            it.findNavController().navigate(action)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding = LayoutMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}