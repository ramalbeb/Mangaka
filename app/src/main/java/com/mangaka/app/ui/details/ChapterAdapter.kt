package com.mangaka.app.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.databinding.ItemChapterBinding

class ChapterAdapter(
    private var items: List<ChapterItem>,
    private val onClick: (ChapterItem) -> Unit,
    private val onDownload: (ChapterItem) -> Unit,
    private val onDelete: (ChapterItem) -> Unit
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    val currentList: List<ChapterItem>
        get() = items

    class ViewHolder(val binding: ItemChapterBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.chapterTitle.text = item.title
        holder.binding.chapterNumber.text = "Chapter ${item.number}"

        // --- UI STATES ---
        when {
            item.isDownloaded -> {
                holder.binding.downloadIcon.visibility = View.GONE
                holder.binding.deleteIcon.visibility = View.VISIBLE
                holder.binding.downloadedIcon.visibility = View.VISIBLE
            }
            item.isDownloading -> {
                holder.binding.downloadIcon.visibility = View.VISIBLE
                holder.binding.downloadIcon.alpha = 0.3f
                holder.binding.deleteIcon.visibility = View.GONE
                holder.binding.downloadedIcon.visibility = View.GONE
            }
            else -> {
                holder.binding.downloadIcon.visibility = View.VISIBLE
                holder.binding.downloadIcon.alpha = 1f
                holder.binding.deleteIcon.visibility = View.GONE
                holder.binding.downloadedIcon.visibility = View.GONE
            }
        }

        // --- CLICK ACTIONS ---
        holder.itemView.setOnClickListener { onClick(item) }
        holder.binding.downloadIcon.setOnClickListener { onDownload(item) }
        holder.binding.deleteIcon.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<ChapterItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
