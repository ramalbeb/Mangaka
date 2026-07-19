package com.mangaka.app.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.databinding.ItemSortOptionBinding

class SortOptionAdapter(
    private val items: List<SortOption>,
    private val current: SortOption,
    private val onClick: (SortOption) -> Unit
) : RecyclerView.Adapter<SortOptionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSortOptionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSortOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = items[position]
        holder.binding.optionText.text = option.label

        holder.binding.checkIcon.visibility =
            if (option == current) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener { onClick(option) }
    }

    override fun getItemCount() = items.size
}
