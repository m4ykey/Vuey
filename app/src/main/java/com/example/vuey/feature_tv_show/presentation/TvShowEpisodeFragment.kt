package com.example.vuey.feature_tv_show.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentTvShowEpisodeBinding
import com.example.vuey.feature_tv_show.presentation.adapter.EpisodeListAdapter
import com.example.vuey.feature_tv_show.presentation.viewmodel.TvShowViewModel
import com.example.vuey.util.network.Resource
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowEpisodeFragment : Fragment() {

    private var _binding : FragmentTvShowEpisodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TvShowViewModel by viewModels()
    private lateinit var episodeListAdapter : EpisodeListAdapter
    private val arguments : TvShowEpisodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodeListAdapter = EpisodeListAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerViewEpisodes.apply {
                adapter = episodeListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }

        viewModel.tvShowSeason(arguments.tvShowId, arguments.seasonNumber)
        viewModel.tvShowSeasons.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> { showLoading() }

                is Resource.Success -> {
                    hideLoading()

                    val tvShowEpisode = response.data!!

                    tvShowEpisode.let { episodeList ->
                        episodeListAdapter.submitEpisode(episodeList.episodes)
                    }
                    with(binding) {
                        txtSeasonNumber.text = getString(R.string.season) + " ${arguments.seasonNumber}"
                    }
                }

                is Resource.Failure -> {
                    hideLoading()
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
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