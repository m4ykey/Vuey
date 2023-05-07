package com.example.vuey.feature_tv_show.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentTvShowBinding
import com.example.vuey.feature_tv_show.presentation.adapter.TvShowAdapter
import com.example.vuey.feature_tv_show.presentation.viewmodel.TvShowViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.Distribution.BucketOptions.Linear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFragment : Fragment() {

    private var _binding : FragmentTvShowBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TvShowViewModel by viewModels()
    private lateinit var tvShowAdapter: TvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvShowAdapter = TvShowAdapter(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.VISIBLE

        with(binding) {

            fabSearch.setOnClickListener { findNavController().navigate(R.id.action_tvShowFragment_to_tvShowSearchFragment) }

            tvShowRecyclerView.apply {
                adapter = tvShowAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            viewModel.getAllTvShows.observe(viewLifecycleOwner) { tvShowList ->
                tvShowAdapter.submitTvShowEntity(tvShowList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}