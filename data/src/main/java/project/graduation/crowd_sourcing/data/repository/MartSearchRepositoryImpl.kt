package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.domain.model.entity.Mart
import project.graduation.crowd_sourcing.domain.repository.MartSearchRepository
import javax.inject.Inject

class MartSearchRepositoryImpl @Inject constructor(
    private val martSearchService: MartSearchService
) : MartSearchRepository {
    
    override suspend fun searchMartByZipcode(zipcode: String, radius: Int): List<Mart> {
        return martSearchService.getSearchMartByZipcode(zipcode, radius).data.map { martDto ->
            Mart(
                sigungu = martDto.sigungu,
                martName = martDto.martName,
                lat = martDto.lat,
                lng = martDto.lng
            )
        }
    }

    override suspend fun searchMartByLocation(lat: Double, lng: Double, radius: Int): List<Mart> {
        return martSearchService.getSearchMartByLocation(lat, lng, radius).data.map { martDto ->
            Mart(
                sigungu = martDto.sigungu,
                martName = martDto.martName,
                lat = martDto.lat,
                lng = martDto.lng
            )
        }
    }

    override suspend fun searchMartByKeyword(keyword: String, radius: Int): List<Mart> {
        return martSearchService.getSearchMartByKeyword(keyword, radius).data.map { martDto ->
            Mart(
                sigungu = martDto.sigungu,
                martName = martDto.martName,
                lat = martDto.lat,
                lng = martDto.lng
            )
        }
    }
} 