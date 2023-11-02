package com.hrishikesh.stockmarketapp.presentation.company_listing

import com.hrishikesh.stockmarketapp.domain.model.CompanyListing

data class CompanyListingsStates(
    val companies : List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = ""
)
