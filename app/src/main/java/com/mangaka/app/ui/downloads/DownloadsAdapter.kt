package com.mangaka.app.ui.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.databinding.ItemDownloadBinding

class DownloadsAdapter(
    private var items: List<DownloadItem>
) : RecyclerView.Adapter<DownloadsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDownloadBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDownloadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.title.text = item.chapterTitle
        holder.binding.progressBar.progress = item.progress
        holder.binding.status.text =
            if (item.isCompleted) "Completed" else "${item.progress}%"
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<DownloadItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
