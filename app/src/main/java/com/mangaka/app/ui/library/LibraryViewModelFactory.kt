package com.mangaka.app.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LibraryViewModelFactory(
    private val useCase: LibraryUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
