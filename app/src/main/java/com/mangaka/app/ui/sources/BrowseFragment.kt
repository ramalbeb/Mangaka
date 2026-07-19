package com.mangaka.app.ui.sources

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mangaka.app.MainActivity
import com.mangaka.app.databinding.FragmentBrowseBinding
import com.mangaka.sources.SourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowseFragment : Fragment() {

    private var _binding: FragmentBrowseBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BrowseAdapter

    private var sourceId: Long = 0L

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sourceId = BrowseFragmentArgs.fromBundle(requireArguments()).sourceId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = BrowseAdapter(emptyList()) { item ->
            val action = BrowseFragmentDirections
                .actionBrowseFragmentToMangaDetailsFragment(
                    sourceId = sourceId,
                    mangaUrl = item.url,
                    mangaTitle = item.title
                )
            findNavController().navigate(action)
        }

        binding.browseRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.browseRecycler.adapter = adapter

        setupSearch()
        loadPopular()
    }

    private fun setupSearch() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim().orEmpty()
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    if (query.isEmpty()) loadPopular()
                    else search(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadPopular() {
        val source = SourceManager.get(sourceId) ?: return

        // Always launch on IO to keep UI responsive, the engine will handle switching to Main if needed
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.e("BROWSE", "Calling getPopularManga for ${source.name}")
                val list = source.getPopularManga(1)

                if (list.isEmpty()) {
                    Log.e("BROWSE", "Popular list empty")
                }

                val uiList = list.map {
                    BrowseItem(
                        id = it.url.hashCode().toLong(),
                        title = it.title,
                        coverUrl = it.coverUrl,
                        url = it.url
                    )
                }

                withContext(Dispatchers.Main) {
                    adapter.submitList(uiList)
                }
            } catch (e: Exception) {
                if (e.message == "CLOUDFLARE") {
                    val activity = requireActivity() as MainActivity
                    val cfUrl = "https://lekmanga.net/manga?page=1"
                    activity.showCloudflare(cfUrl)
                    return@launch
                } else {
                    Log.e("BROWSE", "loadPopular error", e)
                }
            }
        }
    }

    private fun search(query: String) {
        val source = SourceManager.get(sourceId) ?: return

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.e("BROWSE", "Calling search for ${source.name}")
                val list = source.searchManga(query, 1)

                val uiList = list.map {
                    BrowseItem(
                        id = it.url.hashCode().toLong(),
                        title = it.title,
                        coverUrl = it.coverUrl,
                        url = it.url
                    )
                }

                withContext(Dispatchers.Main) {
                    adapter.submitList(uiList)
                }
            } catch (e: Exception) {
                if (e.message == "CLOUDFLARE") {
                    val activity = requireActivity() as MainActivity
                    val cfUrl = "https://lekmanga.net/?s=$query"
                    activity.showCloudflare(cfUrl)
                    return@launch
                } else {
                    Log.e("BROWSE", "search error", e)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
