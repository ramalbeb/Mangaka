package com.mangaka.app.ui.reader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mangaka.app.databinding.ItemReaderPageBinding
import java.io.File

class ReaderAdapter(
    private var items: List<ReaderItem>
) : RecyclerView.Adapter<ReaderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReaderPageBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReaderPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val file = File(item.imagePath)

        if (file.exists()) {
            holder.binding.pageImage.load(file)
        } else {
            holder.binding.pageImage.load(item.imagePath)
        }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<ReaderItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
