package com.alibasoglu.stockmarketapp.domain.repository

import com.alibasoglu.stockmarketapp.domain.model.CompanyListing
import com.alibasoglu.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}
