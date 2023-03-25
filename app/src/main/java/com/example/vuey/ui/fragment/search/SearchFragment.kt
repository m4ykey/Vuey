package com.example.vuey.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchBinding
import com.example.vuey.ui.adapter.SearchViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE

        with(binding) {

            val categories = arrayListOf<Fragment>(
                SearchAlbumFragment()
            )

            val viewPagerAdapter =
                SearchViewPagerAdapter(categories, childFragmentManager, lifecycle)
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position, ->
                when (position) {
                    0 -> tab.text = R.string.album.toString()
                }
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}