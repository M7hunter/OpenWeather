package com.m7.openweather.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.m7.openweather.R
import com.m7.openweather.data.model.Option
import com.m7.openweather.databinding.ItemOptionBinding

class OptionsAdapter(
    private var dataList: List<Option>,
    private val onUnitClicked: (option: Option) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_option,
                parent,
                false
            )
        ).apply {
            itemBinding.root.setOnClickListener {
                onUnitClicked.invoke(dataList[adapterPosition])
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    inner class ViewHolder(val itemBinding: ItemOptionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(itemData: Option) {
            itemBinding.optionTitle = itemData.title
        }
    }
}