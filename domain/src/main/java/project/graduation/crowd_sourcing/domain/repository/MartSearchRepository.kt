package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.Mart

interface MartSearchRepository {
    suspend fun searchMartByZipcode(zipcode: String, radius: Int): List<Mart>
    suspend fun searchMartByLocation(lat: Double, lng: Double, radius: Int): List<Mart>
    suspend fun searchMartByKeyword(keyword: String, radius: Int): List<Mart>
}