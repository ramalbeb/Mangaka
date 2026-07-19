package com.mangaka.app.ui.history

import androidx.lifecycle.*
import com.mangaka.data.repository.HistoryRepository
import com.mangaka.domain.model.HistoryItem
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repo: HistoryRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<HistoryItem>>()
    val items: LiveData<List<HistoryItem>> = _items

    fun load() {
        viewModelScope.launch {
            _items.value = repo.getAll()
        }
    }

    fun delete(item: HistoryItem) {
        viewModelScope.launch {
            repo.delete(item.mangaUrl)
            load()
        }
    }

    fun clear() {
        viewModelScope.launch {
            repo.clear()
            load()
        }
    }
}
