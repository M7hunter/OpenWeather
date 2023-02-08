package com.m7.openweather.ui.search

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.databinding.FragmentSearchBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.util.LocationHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment(private val searchViewModel: SearchViewModel) :
    Fragment() {

    private lateinit var locationHandler: LocationHandler
    private lateinit var layoutBinding: FragmentSearchBinding

    private var optionType = OptionType.CityName
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater,
            R.layout.fragment_search,
            container,
            false
        ).apply {
            layoutBinding = this
            locationHandler = LocationHandler(requireActivity())
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.apply {
            etSearch.requestFocus()

            btnBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            etSearch.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.also { searchText = it }
            }

            btnSearch.setOnClickListener {
                searchText.takeIf { it.isNotBlank() }?.also {
                    when (optionType) {
                        OptionType.CityName -> searchViewModel.getByCityName(it)
                        OptionType.Zip -> searchViewModel.getByZip(it)
                        OptionType.LatLng -> {
                            searchViewModel.getByLatLng(
                                it.substringBefore("/"),
                                it.substringAfter("/")
                            )
                        }
                        OptionType.CurrentLocation -> useCurrentLocation()
                    }
                } ?: run { etSearch.error = getString(R.string.enter_valid_input) }
            }

            rvSearchResults.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = SearchAdapter(searchViewModel.getSearchList(),
                    onOptionClicked = {
                        if (it.optionType == OptionType.CurrentLocation) useCurrentLocation()

                        optionType = it.optionType
                        searchHint = getString(it.optionType.nameRes)
                        etSearch.requestFocus()
                    },
                    onLocationClicked = {
                        searchViewModel.getByLatLng(it.lat, it.lng)
                    })
            }
        }
    }

    private fun useCurrentLocation() {
        locationHandler.getLocation()?.also {
            searchViewModel.getByLatLng(it.latitude.toString(), it.longitude.toString())
        }
            ?: (requireActivity() as BaseActivity<*>)
                .showError("open your GPS and try again")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
            (requireActivity() as BaseActivity<*>)
                .showError(getString(R.string.permissions_denied_error))
        }
    }
}