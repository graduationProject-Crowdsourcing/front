package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity

interface MartSearchRepository {
    suspend fun searchMartByZipcode(zipcode: String, radius: Int): List<MartEntity>
    suspend fun searchMartByLocation(lat: Double, lng: Double, radius: Int): List<MartEntity>
    suspend fun searchMartByKeyword(keyword: String, radius: Int): List<MartEntity>
}