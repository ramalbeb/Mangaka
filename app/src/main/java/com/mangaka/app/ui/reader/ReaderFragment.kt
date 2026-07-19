package com.mangaka.app.ui.reader

import android.os.Bundle
import android.view.*
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.mangaka.app.databinding.FragmentReaderBinding
import com.mangaka.app.di.AppModule
import com.mangaka.domain.model.HistoryItem
import kotlinx.coroutines.launch

class ReaderFragment : Fragment() {

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!

    private val args: ReaderFragmentArgs by navArgs()

    private lateinit var viewModel: ReaderViewModel
    private lateinit var adapter: ReaderAdapter

    private var sourceId: Long = 0L
    private var chapterUrl: String = ""
    private var chapterTitle: String = ""

    private var isPagerMode = true
    private var currentPosition = 0
    private var uiVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sourceId = args.sourceId
        chapterUrl = args.chapterUrl
        chapterTitle = args.chapterTitle

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val context = requireContext().applicationContext
        val db = AppModule.provideDatabase(context)

        val sourceRepo = AppModule.provideSourceRepository()
        val pageRepo = AppModule.providePageRepository(db)
        val readerRepo = AppModule.provideReaderRepository(sourceRepo, pageRepo)
        val useCase = AppModule.provideReaderUseCase(readerRepo)

        val factory = ReaderViewModelFactory(useCase)
        viewModel = ViewModelProvider(this, factory)[ReaderViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = ReaderAdapter(emptyList())
        binding.readerRecycler.adapter = adapter

        setLayoutManager()
        setupTapZones()
        setupScrollListener()

        observeState()
        viewModel.loadChapter(sourceId, chapterUrl, chapterTitle.hashCode().toLong())
    }

    private fun observeState() {
        val appContext = requireContext().applicationContext

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->

                if (state.isLoading) {
                    binding.pageIndicator.text = "Loading..."
                }

                val isOffline = com.mangaka.data.download.DownloadStorage.fileExists(
                    appContext, sourceId, chapterUrl
                )

                val chapterDir = com.mangaka.data.download.DownloadStorage.getChapterDir(
                    appContext, sourceId, chapterUrl
                )

                val uiPages = state.pages.map { page ->
                    val localFile = com.mangaka.data.download.DownloadStorage.getPageFile(
                        chapterDir, page.index
                    )

                    val path = if (isOffline && localFile.exists()) {
                        localFile.absolutePath
                    } else {
                        page.imageUrl
                    }

                    ReaderItem(
                        index = page.index,
                        imagePath = path
                    )
                }

                adapter.submitList(uiPages)
                updatePageIndicator(0, uiPages.size)

                state.error?.let { msg ->
                    binding.pageIndicator.text = "Error: $msg"
                }
            }
        }
    }




    private fun setLayoutManager() {
        if (isPagerMode) {
            val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layout.initialPrefetchItemCount = 3
            binding.readerRecycler.layoutManager = layout
            PagerSnapHelper().attachToRecyclerView(binding.readerRecycler)
        } else {
            val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            layout.initialPrefetchItemCount = 3
            binding.readerRecycler.layoutManager = layout
        }
    }

    private fun setupScrollListener() {
        binding.readerRecycler.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                newState: Int
            ) {
                if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {

                    val lm = recyclerView.layoutManager as LinearLayoutManager
                    val pos = lm.findFirstVisibleItemPosition()
                    currentPosition = pos

                    updatePageIndicator(pos, adapter.itemCount)
                    saveHistory()
                }
            }
        })
    }

    private fun updatePageIndicator(position: Int, total: Int) {
        binding.pageIndicator.text = "Page ${position + 1} / $total"
    }

    private fun setupTapZones() {
        binding.tapOverlay.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val width = v.width
                val x = event.x

                when {
                    x < width * 0.33f -> onTapLeft()
                    x > width * 0.66f -> onTapRight()
                    else -> onTapCenter()
                }
            }
            true
        }
    }

    private fun onTapLeft() {
        val lm = binding.readerRecycler.layoutManager as LinearLayoutManager
        val pos = lm.findFirstVisibleItemPosition()
        if (pos > 0) binding.readerRecycler.smoothScrollToPosition(pos - 1)
    }

    private fun onTapRight() {
        val lm = binding.readerRecycler.layoutManager as LinearLayoutManager
        val pos = lm.findFirstVisibleItemPosition()
        val last = adapter.itemCount - 1
        if (pos < last) binding.readerRecycler.smoothScrollToPosition(pos + 1)
    }

    private fun onTapCenter() {
        uiVisible = !uiVisible
        binding.topBar.visibility = if (uiVisible) View.VISIBLE else View.GONE
        binding.pageIndicator.visibility = if (uiVisible) View.VISIBLE else View.GONE
    }

    private fun saveHistory() {
        val context = requireContext().applicationContext
        val db = AppModule.provideDatabase(context)
        val historyRepo = com.mangaka.data.repository.HistoryRepository(db.historyDao())

        val item = HistoryItem(
            mangaTitle = chapterTitle,
            coverUrl = "",
            sourceId = sourceId,
            mangaUrl = "",
            chapterTitle = chapterTitle,
            chapterUrl = chapterUrl,
            lastPage = currentPosition
        )

        viewLifecycleOwner.lifecycleScope.launch {
            historyRepo.addOrUpdate(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
