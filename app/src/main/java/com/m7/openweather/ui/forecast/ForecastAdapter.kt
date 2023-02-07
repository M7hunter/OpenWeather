package com.m7.openweather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.m7.openweather.R
import com.m7.openweather.data.model.Forecast
import com.m7.openweather.databinding.ItemForecastBinding

class ForecastAdapter(private val list: List<Forecast>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemForecastBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(itemData: Forecast) {
            itemBinding.forecast = itemData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_forecast,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}