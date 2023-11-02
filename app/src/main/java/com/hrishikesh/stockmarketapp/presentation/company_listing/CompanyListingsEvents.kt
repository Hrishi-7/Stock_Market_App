package com.hrishikesh.stockmarketapp.presentation.company_listing

sealed class CompanyListingsEvents{
    object Refresh : CompanyListingsEvents()
    data class OnSearchQueryChange(val query : String) : CompanyListingsEvents()
}
