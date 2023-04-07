package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.local.movie.search.SearchMovie
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.ui.screens.search.SearchMovieFragment
import com.example.vuey.ui.screens.search.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.views.DiffUtils
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movieResult = listOf<SearchMovie>()

    fun submitMovie(newMovie : List<SearchMovie>) {
        val oldMovie = DiffUtils(movieResult, newMovie)
        val result = DiffUtil.calculateDiff(oldMovie)
        movieResult = newMovie
        result.dispatchUpdatesTo(this)
    }

    class MovieViewHolder(private val binding : LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult : SearchMovie) {
            with(binding) {

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                if (movieResult.release_date.isNotEmpty()) {
                    val movieYear = sdf.parse(movieResult.release_date)
                    val formattedDate = outputDateFormat.format(movieYear!!)
                    txtReleaseDate.text = formattedDate
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                txtMovieTitle.text = movieResult.title
                txtDescription.text = movieResult.overview

                imgMovie.load(TMDB_IMAGE + movieResult.poster_path) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.ic_movie_error)
                }

                layoutMovie.setOnClickListener {
                    val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(
                        searchMovie = movieResult
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
        holder.bind(movieResult[position])
    }

    override fun getItemCount(): Int {
        return movieResult.size
    }
}