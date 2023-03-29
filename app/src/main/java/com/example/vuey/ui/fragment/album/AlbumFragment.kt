package com.example.vuey.ui.fragment.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE

        binding.fabSearch.setOnClickListener {
            findNavController().navigate(R.id.action_albumFragment_to_searchFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}