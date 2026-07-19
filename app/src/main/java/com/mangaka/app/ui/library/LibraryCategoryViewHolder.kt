package com.mangaka.app.ui.library

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.R

class LibraryCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.categoryName)
}
