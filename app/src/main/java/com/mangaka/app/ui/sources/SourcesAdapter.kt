package com.mangaka.app.ui.sources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangaka.app.databinding.ItemSourceBinding

class SourcesAdapter(
    private var items: List<SourceItem>,
    private val onClick: (SourceItem) -> Unit
) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSourceBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSourceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.sourceName.text = item.name
        holder.binding.sourceLang.text = item.language
        holder.binding.sourceIcon.load(item.iconUrl)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
