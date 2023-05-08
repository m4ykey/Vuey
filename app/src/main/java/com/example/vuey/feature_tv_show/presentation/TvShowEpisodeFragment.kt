package com.example.vuey.feature_tv_show.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentTvShowEpisodeBinding
import com.example.vuey.feature_tv_show.data.api.season.Episode
import com.example.vuey.feature_tv_show.presentation.adapter.EpisodeAdapter
import com.example.vuey.feature_tv_show.presentation.adapter.EpisodeListAdapter
import com.example.vuey.feature_tv_show.presentation.viewmodel.TvShowViewModel
import com.example.vuey.util.network.Resource
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import com.google.api.Distribution.BucketOptions.Linear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowEpisodeFragment : Fragment() {

    private var _binding : FragmentTvShowEpisodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TvShowViewModel by viewModels()
    private lateinit var episodeListAdapter : EpisodeListAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}