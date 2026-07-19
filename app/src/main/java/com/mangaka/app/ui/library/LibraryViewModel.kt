package com.mangaka.app.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val useCase: LibraryUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<LibraryItem>>()
    val items: LiveData<List<LibraryItem>> = _items

    private val _categories = MutableLiveData<List<LibraryCategory>>()
    val categories: LiveData<List<LibraryCategory>> = _categories

    private val _isGrid = MutableLiveData(true)
    val isGrid: LiveData<Boolean> = _isGrid

    private val _sortOption = MutableLiveData(SortOption.TITLE_ASC)
    val sortOption: LiveData<SortOption> = _sortOption

    private val _filters = MutableLiveData<Set<FilterOption>>(emptySet())
    val filters: LiveData<Set<FilterOption>> = _filters

    private var originalItems: List<LibraryItem> = emptyList()

    init {
        loadLibrary()
    }

    fun loadLibrary() {
        viewModelScope.launch {
            originalItems = useCase.getLibrary()
            buildCategories()
            applyFilters()
            applySorting()
        }
    }

    private fun buildCategories() {
        val cats = mutableListOf<LibraryCategory>()
        cats += LibraryCategory(0, "All", true)

        originalItems
            .map { it.category }
            .distinct()
            .sorted()
            .forEachIndexed { index, name ->
                cats += LibraryCategory(index + 1L, name, false)
            }

        _categories.value = cats
    }

    fun selectCategory(category: LibraryCategory) {
        _categories.value = _categories.value?.map {
            it.copy(isSelected = it.id == category.id)
        }

        _items.value = when (category.name) {
            "All" -> originalItems
            else -> originalItems.filter { it.category == category.name }
        }.let { applyFiltersOn(it) }.let { applySortingOn(it) }
    }

    fun toggleDisplayMode() {
        _isGrid.value = !(_isGrid.value ?: true)
    }

    fun setSort(option: SortOption) {
        _sortOption.value = option
        _items.value = applySortingOn(_items.value ?: emptyList())
    }

    private fun applySorting() {
        _items.value = applySortingOn(_items.value ?: emptyList())
    }

    private fun applySortingOn(list: List<LibraryItem>): List<LibraryItem> {
        return when (_sortOption.value) {
            SortOption.TITLE_ASC -> list.sortedBy { it.title }
            SortOption.TITLE_DESC -> list.sortedByDescending { it.title }
            SortOption.LAST_READ -> list.sortedByDescending { it.lastRead }
            SortOption.LAST_UPDATED -> list.sortedByDescending { it.lastUpdated }
            SortOption.DATE_ADDED -> list.sortedByDescending { it.dateAdded }
            else -> list
        }
    }

    fun toggleFilter(option: FilterOption) {
        val current = _filters.value ?: emptySet()
        _filters.value = if (current.contains(option)) {
            current - option
        } else {
            current + option
        }
        _items.value = applySortingOn(applyFiltersOn(originalItems))
    }

    private fun applyFilters() {
        _items.value = applySortingOn(applyFiltersOn(originalItems))
    }

    private fun applyFiltersOn(source: List<LibraryItem>): List<LibraryItem> {
        var filtered = source

        _filters.value?.forEach { filter ->
            filtered = when (filter) {
                FilterOption.DOWNLOADED -> filtered.filter { it.isDownloaded }
                FilterOption.UNREAD -> filtered.filter { it.unreadCount > 0 }
                FilterOption.COMPLETED -> filtered.filter { it.isCompleted }
                FilterOption.IN_PROGRESS -> filtered.filter { !it.isCompleted && it.unreadCount == 0 }
                FilterOption.FAVORITES -> filtered.filter { it.isFavorite }
            }
        }

        return filtered
    }
}
