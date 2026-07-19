package com.mangaka.app.ui.library

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.R

class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cover: ImageView = view.findViewById(R.id.mangaCover)
    val title: TextView = view.findViewById(R.id.mangaTitle)
}
