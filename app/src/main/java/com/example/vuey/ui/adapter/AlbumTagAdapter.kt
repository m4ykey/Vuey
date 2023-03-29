package com.example.vuey.ui.adapter
//
//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.example.vuey.data.local.album.detail.Tags
//import com.example.vuey.databinding.LayoutAlbumTagsBinding
//import com.example.vuey.util.DiffUtils
//
//class AlbumTagAdapter : RecyclerView.Adapter<AlbumTagAdapter.TagViewHolder>() {
//
//    private var tagResult = listOf<Tags.Tag>()
//
//    fun submitTags(newTag : List<Tags.Tag>) {
//        val oldTag = DiffUtils(tagResult, newTag)
//        val result = DiffUtil.calculateDiff(oldTag)
//        tagResult = newTag
//        result.dispatchUpdatesTo(this)
//    }
//
//    class TagViewHolder(private val binding : LayoutAlbumTagsBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(tagResult : Tags.Tag) {
//            binding.apply {
//                chipTag.text = tagResult.name
//                chipTag.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tagResult.url))
//                    root.context.startActivity(intent)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): TagViewHolder {
//        val binding = LayoutAlbumTagsBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return TagViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
//        holder.bind(tagResult[position])
//    }
//
//    override fun getItemCount(): Int {
//        return tagResult.size
//    }
//}