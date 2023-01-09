package com.alibasoglu.stockmarketapp.data.csv

import com.alibasoglu.stockmarketapp.data.mapper.toIntradayInfo
import com.alibasoglu.stockmarketapp.data.remote.dto.IntradayInfoDto
import com.alibasoglu.stockmarketapp.domain.model.IntradayInfo
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {

    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)  // You drop the first array because first row is just column names
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0)
                    val close = line.getOrNull(4)

                    IntradayInfoDto(
                        timestamp = timestamp ?: return@mapNotNull null,
                        close = close?.toDouble() ?: return@mapNotNull null,
                    ).toIntradayInfo()
                }
                .filter { intradayInfo ->
                    intradayInfo.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                    // Getting only the intraday info of yesterday
                }.sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}
