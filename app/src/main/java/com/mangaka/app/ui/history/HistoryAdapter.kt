package com.mangaka.app.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangaka.app.databinding.ItemHistoryBinding
import com.mangaka.domain.model.HistoryItem

class HistoryAdapter(
    private var items: List<HistoryItem>,
    private val onClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    val currentItems: List<HistoryItem>
        get() = items

    class ViewHolder(val binding: ItemHistoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.cover.load(item.coverUrl)
        holder.binding.title.text = item.mangaTitle
        holder.binding.chapter.text = "Last read: ${item.chapterTitle}"
        holder.binding.page.text = "Page ${item.lastPage + 1}"

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<HistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
