package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.domain.repository.MartSearchRepository
import javax.inject.Inject

class MartSearchRepositoryImpl @Inject constructor(
    private val martSearchService: MartSearchService
) : MartSearchRepository {
    
    override suspend fun searchMartByZipcode(zipcode: String, radius: Int): List<MartEntity> {
        return martSearchService.getSearchMartByZipcode(zipcode, radius).data.map { martDto ->
            MartEntity(
                martId = martDto.martId,
                martName = martDto.martName,
                latitude = martDto.latitude,
                longitude = martDto.longitude,
                sido = martDto.sido,
                sigungu = martDto.sigungu,
                dong = martDto.dong,
                existCommission = martDto.existCommission
            )
        }
    }

    override suspend fun searchMartByLocation(lat: Double, lng: Double, radius: Double): List<MartEntity> {
        return martSearchService.getSearchMartByLocation(lat, lng, radius).data.map { martDto ->
            MartEntity(
                martId = martDto.martId,
                martName = martDto.martName,
                latitude = martDto.latitude,
                longitude = martDto.longitude,
                sido = martDto.sido,
                sigungu = martDto.sigungu,
                dong = martDto.dong,
                existCommission = martDto.existCommission
            )
        }
    }

    override suspend fun searchMartByKeyword(keyword: String, radius: Double): List<MartEntity> {
        return martSearchService.getSearchMartByKeyword(keyword, radius).data.map { martDto ->
            MartEntity(
                martId = martDto.martId,
                martName = martDto.martName,
                latitude = martDto.latitude,
                longitude = martDto.longitude,
                sido = martDto.sido,
                sigungu = martDto.sigungu,
                dong = martDto.dong,
                existCommission = martDto.existCommission
            )
        }
    }
} 