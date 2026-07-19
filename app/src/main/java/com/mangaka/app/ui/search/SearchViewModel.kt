package com.mangaka.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangaka.sources.SourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {

    private val _results = MutableLiveData<List<SearchItem>>(emptyList())
    val results: LiveData<List<SearchItem>> = _results

    fun search(query: String) {
        if (query.isBlank()) {
            _results.value = emptyList()
            return
        }

        viewModelScope.launch {
            val sources = SourceManager.list()

            val allResults = withContext(Dispatchers.IO) {
                sources.map { source ->
                    async {
                        try {
                            val mangaList = source.searchManga(query, page = 1)
                            mangaList.map {
                                SearchItem(
                                    sourceId = source.id,
                                    mangaUrl = it.url,
                                    title = it.title,
                                    coverUrl = it.coverUrl
                                )
                            }
                        } catch (_: Exception) {
                            emptyList()
                        }
                    }
                }.flatMap { it.await() }
            }

            _results.value = allResults
        }
    }
}
