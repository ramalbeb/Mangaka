package com.mangaka.app.ui.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mangaka.app.databinding.FragmentSourcesBinding
import com.mangaka.sources.SourceManager

class SourcesFragment : Fragment() {

    private var _binding: FragmentSourcesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SourcesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sourceList = SourceManager.list().map {
            SourceItem(
                id = it.id,
                name = it.name,
                language = it.lang
            )
        }

        adapter = SourcesAdapter(sourceList) { source ->
            val action = SourcesFragmentDirections
                .actionSourcesFragmentToBrowseFragment(source.id)
            findNavController().navigate(action)
        }

        binding.sourcesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.sourcesRecycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
