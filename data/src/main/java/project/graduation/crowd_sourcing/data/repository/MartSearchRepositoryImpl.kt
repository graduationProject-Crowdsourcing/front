package project.graduation.crowd_sourcing.data.repository

import android.util.Log
import project.graduation.crowd_sourcing.data.mapper.mart.toEntity
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartWorkEntity
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

    override suspend fun searchWorkByMartName(martName : String): List<MartWorkEntity>{
        return martSearchService.getSearchWorkByMartName(martName).data.map { martWorkDto ->
            MartWorkEntity(
                id = martWorkDto.id,
                work = martWorkDto.work,
                workCount = martWorkDto.workCount,
                workpoint = martWorkDto.workpoint,
                workhour = martWorkDto.workhour,
                category = martWorkDto.category,
                item = martWorkDto.item,
                workDate = martWorkDto.workDate,
                itemPrice = martWorkDto.itemPrice,
                workStatus = martWorkDto.workStatus
            )
        }
    }

    override suspend fun getMartList(sigungu: String): Result<List<MartEntity>> {
        return try {
            Log.d("MartRepo", "요청 sigungu = $sigungu")
            val response = martSearchService.getMartList(sigungu)

            // body가 존재하면 처리되도록
            val body = response.body()
            if (body != null && body.status == 200) {
                Log.d("MartRepo", "응답 수신 성공, 마트 수: ${body.data.size}")
                Result.success(body.data.map { it.toEntity() })
            } else {
                Log.e("MartRepo", "응답 실패: code=${response.code()}, status=${body?.status}, message=${body?.message}")
                Result.failure(Exception("마트 리스트 조회 실패: ${body?.message ?: "HTTP ${response.code()}" }"))
            }
        } catch (e: Exception) {
            Log.e("MartRepo", "예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }



}