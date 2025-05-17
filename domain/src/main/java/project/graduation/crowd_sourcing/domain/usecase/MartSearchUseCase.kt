package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.martsearch.Mart
import project.graduation.crowd_sourcing.domain.repository.MartSearchRepository
import javax.inject.Inject

class MartSearchUseCase @Inject constructor(
    private val martSearchRepository: MartSearchRepository
) {
    suspend fun searchMartByLocation(lat: Double, lng: Double, radius: Int): List<Mart> {
        return martSearchRepository.searchMartByLocation(lat, lng, radius)
    }

    suspend fun searchMartByZipcode(zipcode: String, radius: Int): List<Mart> {
        return martSearchRepository.searchMartByZipcode(zipcode, radius)
    }

    suspend fun searchMartByKeyword(keyword: String, radius: Int): List<Mart> {
        return martSearchRepository.searchMartByKeyword(keyword, radius)
    }
} 