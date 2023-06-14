package com.example.vuey.feature_album.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentStatisticsAlbumBinding
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsAlbumFragment : Fragment() {

    private var _binding: FragmentStatisticsAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                coroutineScope {

                    val albumsCount = viewModel.getAlbumCount().firstOrNull() ?: 0
                    val totalTracks = viewModel.getTotalTracks().firstOrNull() ?: 0
                    val totalLength = viewModel.getTotalLength().firstOrNull() ?: 0

                    val albumTimeHour = totalLength / (1000 * 60 * 60)
                    val albumTimeMinute = (totalLength / (1000 * 60)) % 60
                    val albumTimeSeconds = (totalLength / 1000) % 60

//                    txtSongs.text = totalTracks.toString()
//                    txtAlbumsNumber.text = albumsCount.toString()
//                    txtLength.text = if (albumTimeHour == 0) {
//                        String.format("%d min %d ${getString(R.string.sec)}", albumTimeMinute, albumTimeSeconds)
//                    } else if (albumTimeMinute == 0){
//                        String.format("%d ${getString(R.string.hour)}", albumTimeHour)
//                    } else {
//                        String.format("%d ${getString(R.string.hour)} %d min", albumTimeHour, albumTimeMinute)
//                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}