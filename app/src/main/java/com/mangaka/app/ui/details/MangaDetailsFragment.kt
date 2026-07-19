package com.mangaka.app.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.mangaka.app.databinding.FragmentMangaDetailsBinding
import com.mangaka.app.ui.downloads.DownloadItem
import com.mangaka.app.ui.downloads.DownloadRepository
import com.mangaka.sources.SourceManager
import kotlinx.coroutines.launch

class MangaDetailsFragment : Fragment() {

    private var _binding: FragmentMangaDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: MangaDetailsFragmentArgs by navArgs()

    private lateinit var chapterAdapter: ChapterAdapter

    private var sourceId: Long = 0L
    private var mangaUrl: String = ""
    private var mangaTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sourceId = args.sourceId
        mangaUrl = args.mangaUrl
        mangaTitle = args.mangaTitle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMangaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chapterAdapter = ChapterAdapter(
            emptyList(),
            onClick = { chapter ->
                val action = MangaDetailsFragmentDirections
                    .actionMangaDetailsFragmentToReaderFragment(
                        sourceId = sourceId,
                        chapterUrl = chapter.url,
                        chapterTitle = chapter.title
                    )
                findNavController().navigate(action)
            },
            onDownload = { chapter ->
                val item = DownloadItem(
                    mangaTitle = mangaTitle,
                    chapterTitle = chapter.title,
                    sourceId = sourceId,
                    chapterUrl = chapter.url
                )

                chapter.isDownloading = true
                chapterAdapter.notifyDataSetChanged()

                viewLifecycleOwner.lifecycleScope.launch {
                    DownloadRepository.startDownload(requireContext().applicationContext, item)
                }
            },
            onDelete = { chapter ->
                val dir = com.mangaka.data.download.DownloadStorage.getChapterDir(
                    requireContext().applicationContext,
                    sourceId,
                    chapter.url
                )
                dir.deleteRecursively()

                chapter.isDownloaded = false
                chapterAdapter.notifyDataSetChanged()
            }
        )

        binding.chaptersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.chaptersRecycler.adapter = chapterAdapter

        loadDetailsAndChapters()

        // --- DOWNLOAD ALL BUTTON ---
        binding.downloadButton.setOnClickListener {
            chapterAdapter.currentList.forEach { chapter ->
                val item = DownloadItem(
                    mangaTitle = mangaTitle,
                    chapterTitle = chapter.title,
                    sourceId = sourceId,
                    chapterUrl = chapter.url
                )

                chapter.isDownloading = true

                viewLifecycleOwner.lifecycleScope.launch {
                    DownloadRepository.startDownload(requireContext().applicationContext, item)
                }
            }
            chapterAdapter.notifyDataSetChanged()
        }

        // --- LIVE UPDATE FROM DOWNLOADS ---
        viewLifecycleOwner.lifecycleScope.launch {
            DownloadRepository.downloads.collect { list ->
                val updated = chapterAdapter.currentList.map { chapter ->
                    val match = list.find { it.chapterUrl == chapter.url }
                    if (match != null) {
                        chapter.copy(
                            isDownloading = !match.isCompleted,
                            isDownloaded = match.isCompleted,
                            progress = match.progress
                        )
                    } else chapter
                }
                chapterAdapter.submitList(updated)
            }
        }
    }


    private suspend fun saveToLibrary() {
        val context = requireContext().applicationContext
        val db = com.mangaka.app.di.AppModule.provideDatabase(context)
        val mangaRepo = com.mangaka.app.di.AppModule.provideMangaRepository(db)

        val source = SourceManager.get(sourceId) ?: return
        val details = source.getMangaDetails(mangaUrl)

        val manga = com.mangaka.domain.model.Manga(
            id = 0L,
            url = mangaUrl,
            title = details.title,
            coverUrl = details.coverUrl,
            description = details.description,
            author = details.author ?: "",
            artist = details.artist ?: "",
            genres = details.genres ?: emptyList(),
            status = details.status ?: "",
            isFavorite = false,
            isDownloaded = false,
            lastRead = 0L,
            lastUpdated = System.currentTimeMillis(),
            unreadCount = 0
        )

        mangaRepo.insert(manga)

        binding.addToLibraryButton.text = "Added ✓"
        binding.addToLibraryButton.isEnabled = false
    }

    private fun loadDetailsAndChapters() {
        val source = SourceManager.get(sourceId) ?: return

        viewLifecycleOwner.lifecycleScope.launch {

            val details = source.getMangaDetails(mangaUrl)
            val chapters = source.getChapterList(mangaUrl)

            binding.cover.load(details.coverUrl)
            binding.title.text = details.title
            binding.description.text = details.description
            binding.status.text = details.status

            val uiChapters = chapters.mapIndexed { index, ch ->
                val isDownloaded = com.mangaka.data.download.DownloadStorage.fileExists(
                    requireContext().applicationContext,
                    sourceId,
                    ch.url
                )

                ChapterItem(
                    id = index.toLong(),
                    title = ch.title,
                    number = ch.number.toInt(),
                    url = ch.url,
                    isDownloaded = isDownloaded
                )
            }

            chapterAdapter.submitList(uiChapters)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
