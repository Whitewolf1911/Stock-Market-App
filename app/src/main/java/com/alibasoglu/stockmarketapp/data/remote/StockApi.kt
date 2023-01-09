package com.alibasoglu.stockmarketapp.data.remote

import com.alibasoglu.stockmarketapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(@Query("apikey") apikey: String = API_KEY): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("apikey") apikey: String = API_KEY,
        @Query("symbol") symbol: String
    ): CompanyInfoDto

    @GET("query?function=TIME_SERIES_INTRADAY&interval=5min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String = API_KEY
    ): ResponseBody

    companion object {
        const val API_KEY = "AXIFISMHUCEGNTD7"
        const val BASE_URL = "https://alphavantage.co"
    }
}
