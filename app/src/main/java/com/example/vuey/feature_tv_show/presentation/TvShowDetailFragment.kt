package com.example.vuey.feature_tv_show.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentTvShowDetailBinding
import com.example.vuey.feature_movie.data.api.detail.Cast
import com.example.vuey.feature_movie.presentation.adapter.CastAdapter
import com.example.vuey.feature_tv_show.data.api.detail.Genre
import com.example.vuey.feature_tv_show.data.api.detail.Season
import com.example.vuey.feature_tv_show.data.api.detail.SpokenLanguage
import com.example.vuey.feature_tv_show.data.api.season.Episode
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.feature_tv_show.presentation.adapter.EpisodeAdapter
import com.example.vuey.feature_tv_show.presentation.adapter.SeasonsAdapter
import com.example.vuey.feature_tv_show.presentation.viewmodel.TvShowViewModel
import com.example.vuey.util.Constants
import com.example.vuey.util.network.Resource
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.formatVoteAverage
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.api.Distribution.BucketOptions.Linear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowDetailFragment : Fragment() {

    private var _binding: FragmentTvShowDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvShowViewModel by viewModels()
    private val arguments: TvShowDetailFragmentArgs by navArgs()
    private lateinit var castAdapter: CastAdapter
    private lateinit var episodeAdapter: EpisodeAdapter
    private var isTvShowSaved = false

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
        episodeAdapter = EpisodeAdapter()

        viewModel.tvShowCredit(arguments.tvShow.id)
        viewModel.getDetailTvShow(arguments.tvShow.id)

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            recyclerViewEpisode.adapter = episodeAdapter

            imgEpisodes.setOnClickListener {
                findNavController().navigate(R.id.action_tvShowDetailFragment_to_tvShowEpisodeFragment)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
            }, 500)

            val databaseArguments = arguments.tvShowEntity

            val tvShowEntity = TvShowEntity(
                id = databaseArguments.id,
                tvShowName = databaseArguments.tvShowName,
                tvShowOverview = databaseArguments.tvShowOverview,
                tvShowStatus = databaseArguments.tvShowStatus,
                tvShowBackdropPath = databaseArguments.tvShowBackdropPath,
                tvShowPosterPath = databaseArguments.tvShowPosterPath,
                tvShowVoteAverage = databaseArguments.tvShowVoteAverage,
                tvShowFirstAirDate = databaseArguments.tvShowFirstAirDate,
                tvShowGenreList = databaseArguments.tvShowGenreList,
                tvShowEpisodeRun = databaseArguments.tvShowEpisodeRun,
                tvShowSpokenLanguage = databaseArguments.tvShowSpokenLanguage,
                tvShowCast = databaseArguments.tvShowCast,
                tvShowSeason = databaseArguments.tvShowSeason
            )

            viewModel.getTvShowById(databaseArguments.id)
                .observe(viewLifecycleOwner) { tvShow ->
                    isTvShowSaved = if (tvShow == null) {
                        imgSave.setImageResource(R.drawable.ic_save_outlined)
                        false
                    } else {
                        imgSave.setImageResource(R.drawable.ic_save)
                        true
                    }
                }

            imgSave.setOnClickListener {
                isTvShowSaved = !isTvShowSaved
                if (isTvShowSaved) {
                    viewModel.insertTvShow(tvShowEntity)
                    showSnackbar(requireView(), getString(R.string.added_to_library))
                    imgSave.setImageResource(R.drawable.ic_save)
                } else {
                    viewModel.deleteTvShow(tvShowEntity)
                    showSnackbar(requireView(), getString(R.string.removed_from_library))
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                }
            }

            val castList = databaseArguments.tvShowCast.map { cast ->
                Cast(
                    character = cast.castCharacter,
                    name = cast.castName,
                    profile_path = cast.castProfilePath,
                    id = cast.id
                )
            }

            castAdapter.submitCast(castList)

            val genre: List<TvShowEntity.TvShowGenreEntity> = databaseArguments.tvShowGenreList
            val genreList = genre.joinToString(separator = ", ") { it.tvShowGenreName }

            val spokenLanguage: List<TvShowEntity.TvShowSpokenLanguageEntity> =
                databaseArguments.tvShowSpokenLanguage
            val spokenLanguageList =
                spokenLanguage.joinToString(separator = ", ") { it.tvShowSpokenLanguageName }

            txtTvShowTitle.text = databaseArguments.tvShowName
            txtOverview.text = databaseArguments.tvShowOverview
            txtOverviewFull.text = databaseArguments.tvShowOverview
            txtStatus.text = "Status: ${databaseArguments.tvShowStatus}"
            txtVoteAverage.text = databaseArguments.tvShowVoteAverage
            txtInfo.text =
                "${databaseArguments.tvShowEpisodeRun}min • $genreList • ${databaseArguments.tvShowFirstAirDate}"

            if (spokenLanguageList.isEmpty()) {
                txtSpokenLanguages.text = getString(R.string.languages_empty)
            } else {
                txtSpokenLanguages.text = spokenLanguageList
            }

            if (databaseArguments.tvShowBackdropPath.isNotEmpty()) {
                imgBackdrop.load(Constants.TMDB_IMAGE_ORIGINAL + databaseArguments.tvShowBackdropPath) {
                    crossfade(true)
                    crossfade(500)
                }
            } else {
                imgBackdrop.load(Constants.TMDB_IMAGE_ORIGINAL + databaseArguments.tvShowPosterPath) {
                    crossfade(true)
                    crossfade(500)
                }
            }
        }

        viewModel.tvShowCredits.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    val tvShowCredits = response.data!!

                    with(binding) {

                        if (tvShowCredits.cast.isEmpty()) {
                            recyclerViewTopCast.visibility = View.GONE
                            txtEmptyCast.visibility = View.VISIBLE
                        }
                        tvShowCredits.let { castEntries ->
                            castAdapter.submitCast(castEntries.cast)
                        }
                    }
                }

                is Resource.Failure -> {
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
                    hideLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.tvShowSeasons.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> { showLoading() }

                is Resource.Success -> {

                    hideLoading()

                    val tvShowEpisode = response.data!!

                    tvShowEpisode.let { episodeList ->
                        episodeAdapter.submitEpisode(episodeList.episodes)
                    }
                }

                is Resource.Failure -> {
                    hideLoading()
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
                }
            }
        }

        viewModel.detailTvShow.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> { showLoading() }

                is Resource.Success -> {
                    hideLoading()

                    val tvShowDetail = response.data!!

                    val genres: List<Genre> = tvShowDetail.genres
                    val genresList = genres.joinToString(separator = ", ") { it.name }

                    val spokenLanguage: List<SpokenLanguage> = tvShowDetail.spoken_languages
                    val languagesList = spokenLanguage.joinToString(separator = ", ") { it.name }

                    with(binding) {

                        if (tvShowDetail.backdrop_path.isNullOrEmpty().not()) {
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

                        txtVoteAverage.text = tvShowDetail.vote_average.formatVoteAverage()
                        txtTvShowTitle.text = tvShowDetail.name
                        if (tvShowDetail.episode_run_time.isEmpty()) {
                            txtInfo.text = "$genresList • ${
                                DateUtils.formatAirDate(
                                    tvShowDetail.first_air_date
                                )
                            }"
                        } else {
                            txtInfo.text =
                                "${tvShowDetail.episode_run_time[0]}min • $genresList • ${
                                    DateUtils.formatAirDate(
                                        tvShowDetail.first_air_date
                                    )
                                }"
                        }

                        val tvShowOverview = tvShowDetail.overview.ifEmpty {
                            arguments.tvShow.overview
                        }
                        txtOverview.text = tvShowOverview
                        txtOverviewFull.text = tvShowOverview

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

                        spinnerSeason.adapter = SeasonsAdapter(
                            requireContext(),
                            tvShowDetail.seasons,
                            tvShowId = tvShowDetail.id
                        )
                        spinnerSeason.setSelection(0)
                        spinnerSeason.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    p3: Long
                                ) {
                                    val selectedSeasonId =
                                        (spinnerSeason.adapter.getItem(position) as Season).season_number
                                    viewModel.tvShowSeason(tvShowDetail.id, selectedSeasonId)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {}
                            }

                        val seasonEntity = tvShowDetail.seasons.map { season ->
                            TvShowEntity.TvShowSeasonEntity(
                                id = season.id,
                                seasonName = season.name,
                                seasonNumber = season.season_number,
                                seasonEpisodeCount = season.episode_count
                            )
                        }

                        val genreEntity = tvShowDetail.genres.map { genre ->
                            TvShowEntity.TvShowGenreEntity(
                                tvShowGenreName = genre.name,
                                id = genre.id
                            )
                        }

                        val spokenLanguageEntity =
                            tvShowDetail.spoken_languages.map { spokenLanguage ->
                                TvShowEntity.TvShowSpokenLanguageEntity(
                                    tvShowSpokenLanguageName = spokenLanguage.name
                                )
                            }

                        val castEntity = viewModel.tvShowCredits.value!!.data!!.cast.map { cast ->
                            TvShowEntity.TvShowCastEntity(
                                castProfilePath = cast.profile_path,
                                castCharacter = cast.character,
                                castName = cast.name,
                                tvShowId = tvShowDetail.id,
                                id = cast.id
                            )
                        }

                        val tvShowEntity = TvShowEntity(
                            id = tvShowDetail.id,
                            tvShowName = tvShowDetail.name,
                            tvShowOverview = tvShowOverview,
                            tvShowStatus = tvShowDetail.status,
                            tvShowPosterPath = tvShowDetail.poster_path,
                            tvShowBackdropPath = tvShowDetail.backdrop_path,
                            tvShowVoteAverage = tvShowDetail.vote_average.formatVoteAverage(),
                            tvShowFirstAirDate = DateUtils.formatAirDate(tvShowDetail.first_air_date)
                                .toString(),
                            tvShowGenreList = genreEntity,
                            tvShowEpisodeRun = tvShowDetail.episode_run_time[0],
                            tvShowSpokenLanguage = spokenLanguageEntity,
                            tvShowCast = castEntity,
                            tvShowSeason = seasonEntity
                        )

                        viewModel.getTvShowById(arguments.tvShowEntity.id)
                            .observe(viewLifecycleOwner) { tvShow ->
                                isTvShowSaved = if (tvShow == null) {
                                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                                    false
                                } else {
                                    imgSave.setImageResource(R.drawable.ic_save)
                                    true
                                }
                            }

                        imgSave.setOnClickListener {
                            isTvShowSaved = !isTvShowSaved
                            if (isTvShowSaved) {
                                viewModel.insertTvShow(tvShowEntity)
                                showSnackbar(requireView(), getString(R.string.added_to_library))
                                imgSave.setImageResource(R.drawable.ic_save)
                            } else {
                                viewModel.deleteTvShow(tvShowEntity)
                                showSnackbar(
                                    requireView(),
                                    getString(R.string.removed_from_library)
                                )
                                imgSave.setImageResource(R.drawable.ic_save_outlined)
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
                    hideLoading()
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