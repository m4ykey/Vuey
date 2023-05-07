package com.example.vuey.feature_tv_show.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentTvShowSearchBinding
import com.example.vuey.feature_tv_show.presentation.adapter.TvShowAdapter
import com.example.vuey.feature_tv_show.presentation.viewmodel.TvShowViewModel
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowSearchFragment : Fragment() {

    private var _binding : FragmentTvShowSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TvShowViewModel by viewModels()
    private lateinit var tvShowAdapter: TvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvShowAdapter = TvShowAdapter(isFromApi = true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        with(binding) {

            recyclerViewTvShow.apply {
                adapter = tvShowAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchQuery = etSearch.text.toString()
                    if (searchQuery.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_search_query),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.tvShowSearch(searchQuery)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            viewModel.searchTvShow.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        response.data?.let { tvShowList ->
                            tvShowAdapter.submitTvShow(tvShowList)
                        }
                    }
                    is Resource.Failure -> {
                        hideLoading()
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("Unexpected error: ${response.message}")
                            .setPositiveButton("Close") { _, _ -> }
                            .show()
                    }
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