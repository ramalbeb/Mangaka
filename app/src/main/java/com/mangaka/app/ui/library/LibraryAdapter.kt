package com.mangaka.app.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangaka.app.R

class LibraryAdapter(
    private var items: List<LibraryItem>,
    private val isGrid: Boolean = true
) : RecyclerView.Adapter<LibraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val layout = if (isGrid) R.layout.item_library_grid else R.layout.item_library_list
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.cover.load(item.coverUrl) {
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<LibraryItem>?) {
        items = newItems ?: emptyList()
        notifyDataSetChanged()
    }
}
