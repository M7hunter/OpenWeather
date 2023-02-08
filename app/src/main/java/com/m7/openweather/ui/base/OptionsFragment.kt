package com.m7.openweather.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m7.openweather.R
import com.m7.openweather.data.model.Option
import com.m7.openweather.databinding.FragmentOptionsBinding

class OptionsFragment(
    private val options: List<Option>,
    private val onOptionSelected: (option: Option) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOptionsBinding.inflate(inflater).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvOptions.layoutManager = LinearLayoutManager(requireContext())
            rvOptions.adapter = OptionsAdapter(options) {
                onOptionSelected.invoke(it)
                dismiss()
            }
        }
    }
}