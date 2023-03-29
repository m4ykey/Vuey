package com.example.vuey.ui.adapter

//class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
//
//    private var albumResult = listOf<AlbumEntity>()
//
//    fun submitAlbum(newAlbum: List<AlbumEntity>) {
//        val oldAlbum = DiffUtils(albumResult, newAlbum)
//        val result = DiffUtil.calculateDiff(oldAlbum)
//        albumResult = newAlbum
//        result.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): AlbumViewHolder {
//        val binding = LayoutAlbumBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return AlbumViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
//        holder.bind(albumResult[position])
//    }
//
//    override fun getItemCount(): Int {
//        return albumResult.size
//    }
//
//    class AlbumViewHolder(
//        private val binding: LayoutAlbumBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(albumResult: AlbumEntity) {
//            with(binding) {
//                val extraLarge = albumResult.image.find { it.size == "extralarge" }
//                imgAlbum.load(extraLarge?.image) {
//                    crossfade(true)
//                    crossfade(500)
//                    error(R.drawable.album_error)
//                }
//                txtAlbum.text = albumResult.albumName
//                txtArtist.text = albumResult.artist
//
//                layoutAlbum.setOnClickListener {
//
//                }
//            }
//        }
//    }
//}

