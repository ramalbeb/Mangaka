package com.mangaka.app.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.databinding.ItemFilterOptionBinding

class FilterOptionAdapter(
    private val items: List<FilterOption>,
    private val active: Set<FilterOption>,
    private val onToggle: (FilterOption) -> Unit
) : RecyclerView.Adapter<FilterOptionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFilterOptionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = items[position]

        holder.binding.optionText.text = option.label
        holder.binding.checkIcon.visibility =
            if (active.contains(option)) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            onToggle(option)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = items.size
}
