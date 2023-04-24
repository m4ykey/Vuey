package com.example.vuey.ui.screens.tv_show

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.data.remote.response.tv_show.TvShowDetailResponse
import com.example.vuey.databinding.FragmentTvShowSeasonsBinding
import com.example.vuey.ui.adapter.EpisodeAdapter
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowSeasonsFragment : Fragment() {

    private var _binding : FragmentTvShowSeasonsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TvShowViewModel by viewModels()
    private lateinit var episodeAdapter: EpisodeAdapter
    private val arguments : TvShowSeasonsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodeAdapter = EpisodeAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        with(binding) {

            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            recyclerViewEpisodeList.apply {
                adapter = episodeAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.tvShowSeason(tvShowId = arguments.tvShowId, seasonNumber = arguments.season.season_number)
        viewModel.tvShowSeasons.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {

                    val tvShowEpisode = response.data!!

                    with(binding) {
                        txtSeasonNumber.text = getString(R.string.season) + " " + tvShowEpisode.season_number.toString()

                    }

                    tvShowEpisode.let { episodeList ->
                        episodeAdapter.submitEpisode(episodeList.episodes)
                    }

                }
                is Resource.Failure -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}