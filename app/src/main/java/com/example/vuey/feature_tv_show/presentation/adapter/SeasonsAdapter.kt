package com.example.vuey.feature_tv_show.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.example.vuey.R
import com.example.vuey.feature_tv_show.data.api.detail.Season
import com.example.vuey.feature_tv_show.data.database.entity.TvShowSeasonEntity
import java.lang.IndexOutOfBoundsException

class SeasonsAdapter(
    private val context : Context,
    private val seasonResult : List<Season>,
    private val tvShowId : Int
    ) : SpinnerAdapter {

    private val filterSeasons = seasonResult.filter { it.season_number != 0 }

    override fun registerDataSetObserver(p0: DataSetObserver?) {}
    override fun unregisterDataSetObserver(p0: DataSetObserver?) {}
    override fun hasStableIds(): Boolean { return true }
    override fun isEmpty(): Boolean { return seasonResult.isEmpty() }
    override fun getItemViewType(position: Int): Int { return 0 }
    override fun getViewTypeCount(): Int { return 1 }

    override fun getCount(): Int {
        return if (filterSeasons.isNotEmpty()) {
            filterSeasons.size
        } else {
            1
        }
    }

    override fun getItem(position: Int): Season? {
        return if (filterSeasons.isNullOrEmpty()) {
            null
        } else {
            filterSeasons[position]
        }
    }

    override fun getItemId(position: Int): Long {
        return if (filterSeasons.isNotEmpty()) {
            filterSeasons[position].id.toLong()
        } else {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, viewGroup, false)
        val seasonsList = filterSeasons ?: emptyList()
        if (seasonsList.isNotEmpty()) {
            view.findViewById<TextView>(android.R.id.text1)?.text = context.getString(R.string.season) + " ${filterSeasons[position].season_number}"
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun getDropDownView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, viewGroup, false)
        view.findViewById<TextView>(android.R.id.text1)?.text = context.getString(R.string.season) + " ${filterSeasons[position].season_number}"
        return view
    }
}
