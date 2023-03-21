package com.example.vuey

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var albumAdapter: AlbumAdapter
    private val viewModel : AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumAdapter = AlbumAdapter()
        binding.recyclerView.adapter = albumAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchAlbum(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        viewModel.albumResponse.observe(this) { response ->
            response.let {
                albumAdapter.submitAlbum(it)
            }
        }
    }
}