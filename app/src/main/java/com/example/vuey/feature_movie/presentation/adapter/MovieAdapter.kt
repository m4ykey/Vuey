package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.databinding.LayoutMovieBinding
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.PreDrawListener

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
                    is MovieList -> {
                        val preDraw = PreDrawListener(imgMovie, layoutMovie)
                        imgMovie.viewTreeObserver.addOnPreDrawListener(preDraw)
                        txtMovieTitle.text = movie.title
                        txtMovieOverview.text = movie.overview
                    }
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