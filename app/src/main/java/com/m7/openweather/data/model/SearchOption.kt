package com.m7.openweather.data.model

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.m7.openweather.R

data class SearchOption(
    @StringRes val name: Int,
    val optionType: OptionType = OptionType.CityName,
    override val type: SearchType = SearchType.SearchOption,
) : Search() {
    var selected = false
}

enum class OptionType(@StringRes val nameRes: Int, @DimenRes val padding: Int) {
    CityName(R.string.city_name, R.dimen.option_padding_city_name),
    Zip(R.string.zip_code, R.dimen.option_padding_zip),
    LatLng(R.string.lat_lng, R.dimen.option_padding_lat_lng),
    CurrentLocation(R.string.current_location, R.dimen.option_padding_current_location)
}
