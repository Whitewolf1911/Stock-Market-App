package com.alibasoglu.stockmarketapp.data.repository

import com.alibasoglu.stockmarketapp.data.csv.CSVParser
import com.alibasoglu.stockmarketapp.data.local.StockDatabase
import com.alibasoglu.stockmarketapp.data.mapper.toCompanyListing
import com.alibasoglu.stockmarketapp.data.mapper.toCompanyListingEntity
import com.alibasoglu.stockmarketapp.data.remote.StockApi
import com.alibasoglu.stockmarketapp.domain.model.CompanyListing
import com.alibasoglu.stockmarketapp.domain.repository.StockRepository
import com.alibasoglu.stockmarketapp.util.Resource
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { companyListingEntity -> companyListingEntity.toCompanyListing() }
            ))

            // You need the check query because it might be something like xyz which doesn't exist
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao.searchCompanyListing("")
                        .map { companyListingEntity -> companyListingEntity.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}
