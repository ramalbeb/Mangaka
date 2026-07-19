package com.mangaka.app.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangaka.app.databinding.DialogSortBinding

class SortBottomSheet(
    private val current: SortOption,
    private val onSelect: (SortOption) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogSortBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val options = SortOption.entries.toTypedArray()

        binding.sortList.adapter = SortOptionAdapter(options.toList(), current) { selected ->
            onSelect(selected)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
