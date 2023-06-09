package com.example.vuey.feature_movie.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutCastBinding
import com.example.vuey.feature_movie.data.remote.model.Cast
import com.example.vuey.util.Constants.TMDB_IMAGE_ORIGINAL
import com.example.vuey.util.utils.DiffUtils

class CastAdapter : RecyclerView.Adapter<CastAdapter.CreditViewHolder>() {

    private var casts = listOf<Cast>()

    fun submitCast(newData : List<Cast>) {
        val oldData = casts.toList()
        casts = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val binding = LayoutCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(casts[position])
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    class CreditViewHolder(private val binding : LayoutCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(castResult : Cast) {
            with(binding) {
                txtActorName.text = castResult.name
                if (castResult.profile_path != null) {
                    imgCast.load(TMDB_IMAGE_ORIGINAL + castResult.profile_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgCast.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }
}