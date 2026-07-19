package com.mangaka.app.ui.sources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangaka.app.databinding.ItemBrowseMangaBinding

class BrowseAdapter(
    private var items: List<BrowseItem>,
    private val onClick: (BrowseItem) -> Unit
) : RecyclerView.Adapter<BrowseAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBrowseMangaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBrowseMangaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.cover.load(item.coverUrl)
        holder.binding.title.text = item.title

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<BrowseItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
