package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.databinding.LayoutCastBinding
import com.example.vuey.feature_movie.data.remote.model.CastDetail
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.utils.DiffUtils

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var cast = emptyList<CastDetail>()

    fun submitCast(newData : List<CastDetail>) {
        val oldData = cast.toList()
        cast = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding : LayoutCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast : CastDetail) {
            with(binding) {
                txtActorName.text = cast.name
                imgCast.load(TMDB_IMAGE_ORIGINAL + cast.profilePath) {
                    crossfade(true)
                    crossfade(500)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastAdapter.CastViewHolder {
        val binding = LayoutCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastAdapter.CastViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int {
        return cast.size
    }
}