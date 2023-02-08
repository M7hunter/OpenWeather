package com.m7.openweather.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.databinding.ItemSearchOptionBinding
import com.m7.openweather.databinding.ItemSearchResultBinding
import com.m7.openweather.databinding.ItemSearchTitleBinding

class SearchAdapter(
    private val searchList: List<Search>,
    private val onOptionClicked: (SearchOption) -> Unit,
    private val onLocationClicked: (SearchResult) -> Unit,
) :
    RecyclerView.Adapter<SearchAdapter.MainViewHolder>() {

    abstract class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(itemData: Search)
    }

    class TitleViewHolder(private val itemBinding: ItemSearchTitleBinding) :
        MainViewHolder(itemBinding.root) {
        override fun bind(itemData: Search) {
            itemBinding.title = itemData as SearchTitle
        }
    }

    private var selectedOptionPos = 1

    inner class OptionViewHolder(private val itemBinding: ItemSearchOptionBinding) :
        MainViewHolder(itemBinding.root) {
        override fun bind(itemData: Search) {
            itemBinding.apply {
                (itemData as SearchOption).also {
                    it.selected = selectedOptionPos == adapterPosition
                    (root as MaterialCardView).strokeWidth = if (it.selected) root.context.resources.getDimension(R.dimen.option_stoke_width_selected).toInt() else 0
                    option = it
                }

                root.setOnClickListener {
                    if (selectedOptionPos != adapterPosition) {
                        notifyItemChanged(selectedOptionPos)
                        selectedOptionPos = adapterPosition
                        notifyItemChanged(adapterPosition)
                    }

                    onOptionClicked.invoke(itemData)
                }
            }
        }
    }

    inner class ResultViewHolder(private val itemBinding: ItemSearchResultBinding) :
        MainViewHolder(itemBinding.root) {
        override fun bind(itemData: Search) {
            itemBinding.apply {
                result = itemData as SearchResult

                root.setOnClickListener {
                    onLocationClicked.invoke(itemData)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = searchList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        when (viewType) {
            SearchType.SearchTitle.ordinal ->
                TitleViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_search_title,
                        parent,
                        false
                    )
                )
            SearchType.SearchOption.ordinal ->
                OptionViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_search_option,
                        parent,
                        false
                    )
                )

            else -> {
                ResultViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_search_result,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemCount(): Int = searchList.size
}