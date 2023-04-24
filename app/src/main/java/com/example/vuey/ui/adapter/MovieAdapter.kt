package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.database.model.MovieEntity
import com.example.vuey.data.models.movie.search.SearchMovie
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.ui.screens.movie.MovieFragmentDirections
import com.example.vuey.ui.screens.movie.MovieViewModel
import com.example.vuey.ui.screens.movie.SearchMovieFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.views.DiffUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter(
    private val isFromApi: Boolean,
    private val viewModel: MovieViewModel
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

        fun bind(movieEntityResult: MovieEntity, viewModel: MovieViewModel) {

            with(binding) {

                val searchMovie = SearchMovie(
                    backdrop_path = movieEntityResult.backdrop_path,
                    id = movieEntityResult.id,
                    overview = movieEntityResult.overview,
                    poster_path = movieEntityResult.poster_path,
                    release_date = movieEntityResult.release_date,
                    title = movieEntityResult.title,
                    vote_average = movieEntityResult.vote_average.replace(",", ".").toDouble(),
                    vote_count = movieEntityResult.vote_count
                )

                txtMovieTitle.text = movieEntityResult.title
                txtDescription.text = movieEntityResult.overview
                txtReleaseDate.text = movieEntityResult.release_date
                txtVoteAverage.text = movieEntityResult.vote_average

                if (movieEntityResult.poster_path != null) {
                    imgMovie.load(TMDB_IMAGE + movieEntityResult.poster_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgMovie.setImageResource(R.drawable.ic_movie_error)
                }

                layoutMovie.setOnLongClickListener {
                    MaterialAlertDialogBuilder(root.context)
                        .setTitle(R.string.delete_movie)
                        .setMessage(root.context.getString(R.string.delete_movie_message) + " ${movieEntityResult.title}?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            viewModel.deleteMovie(movieEntityResult)
                        }
                        .setNegativeButton(R.string.no) { _, _ -> }
                        .show()
                    true
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

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

                val voteAverage = movieResult.vote_average
                val formattedVote = String.format("%.1f", voteAverage)

                val movieEntity = MovieEntity(
                    backdrop_path = movieResult.backdrop_path.toString(),
                    id = movieResult.id,
                    overview = movieResult.overview,
                    poster_path = movieResult.poster_path.toString(),
                    release_date = movieResult.release_date,
                    runtime = "",
                    title = movieResult.title,
                    vote_average = formattedVote,
                    vote_count = movieResult.vote_count,
                    spokenLanguage = emptyList(),
                    genre = emptyList(),
                    cast = emptyList()
                )

                if (movieResult.release_date != null && !movieResult.release_date.isNullOrEmpty()) {
                    val movieYear = sdf.parse(movieResult.release_date)
                    val formattedDate = outputDateFormat.format(movieYear!!)
                    txtReleaseDate.text = formattedDate
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                txtMovieTitle.text = movieResult.title
                txtDescription.text = movieResult.overview
                txtVoteAverage.text = formattedVote

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
            holder.bind(movieEntityResult[position], viewModel)
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