package com.mangaka.app.ui.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangaka.app.databinding.FragmentHistoryBinding
import com.mangaka.app.di.AppModule
import com.mangaka.data.repository.HistoryRepository


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val context = requireContext().applicationContext
        val db = AppModule.provideDatabase(context)
        val repo = HistoryRepository(db.historyDao())
        val factory = HistoryViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        adapter = HistoryAdapter(emptyList()) { item ->
            val action = HistoryFragmentDirections
                .actionHistoryFragmentToReaderFragment(
                    sourceId = item.sourceId,
                    chapterUrl = item.chapterUrl,
                    chapterTitle = item.chapterTitle
                )
            findNavController().navigate(action)
        }

        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = adapter

        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val item = adapter.currentItems[vh.bindingAdapterPosition]
                viewModel.delete(item)
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(binding.historyRecycler)

        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.load()
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.mangaka.app.R.menu.history_menu, menu)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.mangaka.app.R.id.action_clear_history -> {
                viewModel.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
