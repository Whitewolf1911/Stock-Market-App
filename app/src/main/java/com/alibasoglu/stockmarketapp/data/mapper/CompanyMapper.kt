package com.alibasoglu.stockmarketapp.data.mapper

import com.alibasoglu.stockmarketapp.data.local.CompanyListingEntity
import com.alibasoglu.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.alibasoglu.stockmarketapp.domain.model.CompanyInfo
import com.alibasoglu.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        name = name ?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}
