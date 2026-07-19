package com.mangaka.app.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.content.res.AppCompatResources
import com.mangaka.app.databinding.FragmentLibraryBinding
import com.mangaka.app.di.AppModule
import com.mangaka.app.R

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LibraryAdapter
    private lateinit var viewModel: LibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // --- Build ViewModel with DI ---
        val context = requireContext().applicationContext
        val db = AppModule.provideDatabase(context)
        val mangaRepo = AppModule.provideMangaRepository(db)
        val useCase = AppModule.provideLibraryUseCase(mangaRepo)
        val factory = LibraryViewModelFactory(useCase)

        viewModel = ViewModelProvider(this, factory)[LibraryViewModel::class.java]

        // --- Recycler setup ---
        adapter = LibraryAdapter(emptyList(), isGrid = true)
        binding.libraryRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.libraryRecycler.adapter = adapter

        // --- Category tabs ---
        val categoryAdapter = LibraryCategoryAdapter(emptyList()) { category ->
            viewModel.selectCategory(category)
        }
        binding.categoryTabs.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
        }

        // --- Observers ---
        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.categories.observe(viewLifecycleOwner) { list ->
            categoryAdapter.submitList(list)
        }

        // --- Toolbar actions ---
        binding.libraryToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.action_filter -> {
                    FilterBottomSheet(viewModel.filters.value ?: emptySet()) { option ->
                        viewModel.toggleFilter(option)
                    }.show(parentFragmentManager, "filters")
                    true
                }

                R.id.action_sort -> {
                    SortBottomSheet(viewModel.sortOption.value!!) { selected ->
                        viewModel.setSort(selected)
                    }.show(parentFragmentManager, "sort")
                    true
                }

                R.id.action_display -> {
                    viewModel.toggleDisplayMode()
                    true
                }

                else -> false
            }
        }

        // --- Grid/List toggle ---
        viewModel.isGrid.observe(viewLifecycleOwner) { isGrid ->
            adapter = LibraryAdapter(viewModel.items.value ?: emptyList(), isGrid)
            binding.libraryRecycler.adapter = adapter

            binding.libraryRecycler.layoutManager =
                if (isGrid) GridLayoutManager(requireContext(), 3)
                else LinearLayoutManager(requireContext())

            val icon = if (isGrid)
                android.R.drawable.ic_menu_gallery
            else
                android.R.drawable.ic_menu_agenda

            binding.libraryToolbar.menu.findItem(R.id.action_display).icon =
                AppCompatResources.getDrawable(requireContext(), icon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
