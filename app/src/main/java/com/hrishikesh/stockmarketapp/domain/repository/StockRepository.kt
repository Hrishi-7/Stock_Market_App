package com.hrishikesh.stockmarketapp.domain.repository

import com.hrishikesh.stockmarketapp.domain.model.CompanyListing
import com.hrishikesh.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote : Boolean,
        query : String
    ) : Flow<Resource<List<CompanyListing>>>
}