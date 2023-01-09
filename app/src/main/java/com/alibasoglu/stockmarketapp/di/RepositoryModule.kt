package com.alibasoglu.stockmarketapp.di

import com.alibasoglu.stockmarketapp.data.csv.CSVParser
import com.alibasoglu.stockmarketapp.data.csv.CompanyListingsParser
import com.alibasoglu.stockmarketapp.data.csv.IntradayInfoParser
import com.alibasoglu.stockmarketapp.data.repository.StockRepositoryImpl
import com.alibasoglu.stockmarketapp.domain.model.CompanyListing
import com.alibasoglu.stockmarketapp.domain.model.IntradayInfo
import com.alibasoglu.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // In here you are telling Hilt to how to inject CSV parser to StockRepositoryImpl
    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository

}
