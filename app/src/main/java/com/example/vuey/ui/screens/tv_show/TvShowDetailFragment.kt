package com.example.vuey.ui.screens.tv_show

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.data.models.tv_show.Genre
import com.example.vuey.data.models.tv_show.SpokenLanguage
import com.example.vuey.databinding.FragmentTvShowDetailBinding
import com.example.vuey.ui.adapter.CastAdapter
import com.example.vuey.ui.adapter.SeasonsAdapter
import com.example.vuey.util.Constants
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class TvShowDetailFragment : Fragment() {

    private var _binding: FragmentTvShowDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvShowViewModel by viewModels()
    private val tvArguments: TvShowDetailFragmentArgs by navArgs()
    private lateinit var castAdapter: CastAdapter
    private lateinit var seasonsAdapter: SeasonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        castAdapter = CastAdapter()
        val tvShowId = tvArguments.tvShow.id
        seasonsAdapter = SeasonsAdapter(tvShowId = tvShowId)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        with(binding) {

            imgBack.setOnClickListener { findNavController().navigateUp() }

            recyclerViewTopCast.apply {
                adapter = castAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            recyclerViewSeasons.apply {
                adapter = seasonsAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        viewModel.getDetailTvShow(tvArguments.tvShow.id)
        viewModel.detailTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()

                    val tvShowDetail = response.data!!
                    Log.i("TvShowId", "onViewCreated: ${tvShowDetail.id}")

                    val voteAverage = tvShowDetail.vote_average
                    val formattedVoteAverage = String.format("%.1f", voteAverage)

                    val genres: List<Genre> = tvShowDetail.genres
                    val genresList = genres.joinToString(separator = ", ") { it.name }

                    val tvShowYear = sdf.parse(tvShowDetail.first_air_date)
                    val formattedDate = outputDateFormat.format(tvShowYear!!)

                    val spokenLanguage: List<SpokenLanguage> = tvShowDetail.spoken_languages
                    val languagesList = spokenLanguage.joinToString(separator = ", ") { it.name }

                    with(binding) {

                        if (!tvShowDetail.backdrop_path.isNullOrEmpty()) {
                            imgBackdrop.load(Constants.TMDB_IMAGE_ORIGINAL + tvShowDetail.backdrop_path) {
                                crossfade(true)
                                crossfade(500)
                            }
                        } else {
                            imgBackdrop.load(Constants.TMDB_IMAGE_ORIGINAL + tvShowDetail.poster_path) {
                                crossfade(true)
                                crossfade(500)
                            }
                        }
                        txtVoteAverage.text = formattedVoteAverage
                        txtVoteCount.text = " | " + tvShowDetail.vote_count.toString()
                        txtTvShowTitle.text = tvShowDetail.name
                        if (tvShowDetail.episode_run_time.isEmpty()) {
                            txtInfo.text = "$genresList • $formattedDate"
                        } else {
                            txtInfo.text =
                                "${tvShowDetail.episode_run_time[0]}min • $genresList • $formattedDate"
                        }

                        txtOverview.text = tvArguments.tvShow.overview
                        txtOverviewFull.text = tvArguments.tvShow.overview
                        linearLayoutOverview.setOnClickListener {
                            if (txtOverviewFull.visibility == View.GONE) {
                                txtOverview.visibility = View.GONE
                                txtOverviewFull.visibility = View.VISIBLE
                            } else {
                                txtOverview.visibility = View.VISIBLE
                                txtOverviewFull.visibility = View.GONE
                            }
                        }

                        if (languagesList.isEmpty()) {
                            txtSpokenLanguages.text = getString(R.string.languages_empty)
                        } else {
                            txtSpokenLanguages.text = languagesList
                        }
                        txtStatus.text = "Status: ${tvShowDetail.status}"

                        tvShowDetail.let { season ->
                            seasonsAdapter.submitSeason(season.seasons)
                        }
                    }
                }

                is Resource.Failure -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Tv Show Detail Error")
                        .setMessage("${response.message}")
                        .show()
                    hideLoading()
                }
            }
        }

        viewModel.tvShowCredit(tvArguments.tvShow.id)
        viewModel.tvShowCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    val tvShowCredits = response.data!!

                    if (tvShowCredits.cast.isEmpty()) {
                        binding.recyclerViewTopCast.visibility = View.GONE
                        binding.txtEmptyCast.visibility = View.VISIBLE
                    }
                    tvShowCredits.let { castEntries ->
                        castAdapter.submitCast(castEntries.cast)
                    }
                    Log.i("CastEntries", "onViewCreated: ${tvShowCredits.cast}")
                }

                is Resource.Failure -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Cast Error")
                        .setMessage("${response.message}")
                        .show()
                    hideLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}