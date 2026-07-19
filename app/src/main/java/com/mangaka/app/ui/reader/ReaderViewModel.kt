package com.mangaka.app.ui.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangaka.reader.ReaderUseCase
import com.mangaka.reader.ReaderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val useCase: ReaderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState())
    val state: StateFlow<ReaderState> = _state

    fun loadChapter(sourceId: Long, chapterUrl: String, chapterId: Long) {
        viewModelScope.launch {
            _state.value = ReaderState(isLoading = true)

            try {
                val pages = useCase.loadChapterPages(sourceId, chapterUrl, chapterId)
                _state.value = ReaderState(pages = pages)
            } catch (e: Exception) {
                _state.value = ReaderState(error = e.message)
            }
        }
    }
}
