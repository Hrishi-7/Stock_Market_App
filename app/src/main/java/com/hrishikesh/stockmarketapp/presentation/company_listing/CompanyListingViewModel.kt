package com.hrishikesh.stockmarketapp.presentation.company_listing

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishikesh.stockmarketapp.domain.repository.StockRepository
import com.hrishikesh.stockmarketapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyListingViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel(){
    var state by mutableStateOf(CompanyListingsStates())
    private var searchJob : Job? = null
    fun onEvent(event: CompanyListingsEvents){
        when(event){
            is CompanyListingsEvents.Refresh ->{
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvents.OnSearchQueryChange ->{
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query : String = state.searchQuery.lowercase(),
        fetchFromRemote : Boolean = false
    ){
        viewModelScope.launch {
            repository.getCompanyListing(fetchFromRemote, query)
                .collect{ result->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let{listings->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading ->{
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}