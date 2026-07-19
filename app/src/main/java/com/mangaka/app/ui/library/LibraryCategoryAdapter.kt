package com.mangaka.app.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.R

class LibraryCategoryAdapter(
    private var categories: List<LibraryCategory>,
    private val onCategoryClick: (LibraryCategory) -> Unit
) : RecyclerView.Adapter<LibraryCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_tab, parent, false)
        return LibraryCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryCategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.name.text = category.name
        holder.name.alpha = if (category.isSelected) 1f else 0.5f

        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size

    fun submitList(newList: List<LibraryCategory>) {
        categories = newList
        notifyDataSetChanged()
    }
}
