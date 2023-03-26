package com.example.vuey.ui.fragment.album

import android.content.Intent
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.vuey.R
import com.example.vuey.data.local.database.model.AlbumEntity
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.ui.adapter.AlbumTagAdapter
import com.example.vuey.ui.adapter.TrackListAdapter
import com.example.vuey.util.Constants.LUMINANCE_VALUE
import com.example.vuey.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.*

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by viewModels()
    private val arguments: DetailAlbumFragmentArgs by navArgs()
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var trackListAdapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE

        binding.imgBack.setOnClickListener { findNavController().navigateUp() }

        val extraLarge = arguments.albums.image.find { it.size == "extralarge" }

        val luminanceJob = lifecycleScope.launch(Dispatchers.IO) {
            val bitmap =
                Glide.with(requireContext()).asBitmap().load(extraLarge!!.image).submit().get()
            val palette = Palette.from(bitmap).generate()
            val dominantColor = palette.getDominantColor(Color.GRAY)

            val r = Color.red(dominantColor)
            val g = Color.green(dominantColor)
            val b = Color.blue(dominantColor)

            val luminance = (0.2126 * r + 0.7152 * g + 0.0722 * b) / 255.0

            withContext(Dispatchers.Main) {
                binding.apply {

                    txtAlbum.text = arguments.albums.albumName
                    txtArtist.text = arguments.albums.artist

                    txtAlbum.setTextColor(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    txtArtist.setTextColor(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    txtPlaycount.setTextColor(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    txtListeners.setTextColor(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)

                    imgPlaycount.setColorFilter(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    imgBack.setColorFilter(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    imgArtistSite.setColorFilter(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)
                    imgListeners.setColorFilter(if (luminance < LUMINANCE_VALUE) Color.WHITE else Color.BLACK)

                }
            }
        }

        viewModel.getInfo(arguments.albums.artist, arguments.albums.albumName)
        viewModel.albumDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    hideLoading()
                    setupRecyclerView()

                    val albumDetail = response.data

                    val saveAlbumToDatabase = AlbumEntity(
                        mbid = arguments.albums.mbid,
                        artist = arguments.albums.artist,
                        albumName = arguments.albums.albumName,
                        url = arguments.albums.url,
                        listeners = albumDetail!!.listeners,
                        playcount = albumDetail.playcount,
                        wikiContent = albumDetail.wiki.content,
                        wikiSummary = albumDetail.wiki.summary,
                        image = listOf(AlbumEntity.Image(
                            image = arguments.albums.image[3].image,
                            size = arguments.albums.image[3].size
                        )),
                        tracks = albumDetail.tracks.track.map { track ->
                            AlbumEntity.Track(
                                duration = track.duration,
                                trackName = track.trackName,
                                url = track.url,
                                attr = AlbumEntity.Track.Attr(rank = track.attr.rank),
                                artist = AlbumEntity.Artist(
                                    mbid = track.artist.mbid,
                                    name = track.artist.name,
                                    url = track.artist.url
                                )
                            )
                        },
                        tags = albumDetail.tags.tag.map { tag ->
                            AlbumEntity.Tag(
                                name = tag.name,
                                url = tag.url
                            )
                        })
                    viewModel.insertAlbum(saveAlbumToDatabase)
                    //Toast.makeText(requireContext(), "Dodano do bazy danych", Toast.LENGTH_SHORT).show()

                    with(binding) {

                        var isActive = false
                        imgDelete.setOnClickListener {
                            val deleteAlbum = MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.delete_album)
                                .setMessage(R.string.delete_album)
                                .setPositiveButton(R.string.yes) { _, _ ->
                                    viewModel.deleteAlbum(saveAlbumToDatabase)
                                    isActive = true
                                    imgDelete.setImageResource(R.drawable.ic_restore)
                                }
                                .setNegativeButton(R.string.no) { _, _ -> }
                                .create()
                            deleteAlbum.show()

                        }

                        // LinearLayout with summary and content
                        if (albumDetail.wiki.content == null) {
                            expandableLinearLayout.visibility = View.GONE
                            txtAlbumUnavailable.visibility = View.VISIBLE
                        }
                        expandableLinearLayout.setOnClickListener {
                            if (txtContent.visibility == View.GONE) {
                                imgExpand.setImageResource(R.drawable.ic_arrow_up)
                                TransitionManager.beginDelayedTransition(
                                    expandableLinearLayout,
                                    AutoTransition()
                                )
                                txtContent.visibility = View.VISIBLE
                                txtSummary.visibility = View.GONE
                            } else {
                                TransitionManager.beginDelayedTransition(
                                    expandableLinearLayout,
                                    AutoTransition()
                                )
                                txtContent.visibility = View.GONE
                                txtSummary.visibility = View.VISIBLE
                                imgExpand.setImageResource(R.drawable.ic_arrow_down)
                            }
                        }

                        // TextViews
                        txtContent.text = albumDetail.wiki.content
                        txtSummary.text = albumDetail.wiki.summary
                        txtPlaycount.text = albumDetail.playcount
                        txtListeners.text = albumDetail.listeners

                        // Icons
                        imgArtistSite.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(albumDetail.url))
                            startActivity(intent)
                        }

                        // RecyclerViews
                        albumDetail.let { tagList ->
                            tagAdapter.submitTags(tagList.tags.tag)
                        }
                        albumDetail.let { trackList ->
                            trackListAdapter.submitTrack(trackList.tracks.track)
                        }

                        // Images
                        imgAlbum.load(extraLarge!!.image)
                        imgBackgroundAlbum.apply {
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(extraLarge.image)
                                .override(400, 400)
                                .let { request ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        request.into(this)
                                        setRenderEffect(
                                            RenderEffect.createBlurEffect(
                                                20.0f, 20.0f, Shader.TileMode.CLAMP
                                            )
                                        )
                                    } else {
                                        request
                                            .apply(
                                                RequestOptions
                                                    .bitmapTransform(BlurTransformation(20))
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            )
                                            .into(this)
                                    }
                                }
                        }
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        tagAdapter = AlbumTagAdapter()
        trackListAdapter = TrackListAdapter()
        binding.recyclerViewTracks.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.recyclerViewTags.apply {
            adapter = tagAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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