package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import project.graduation.crowd_sourcing.data.service.SearchService
import project.graduation.crowd_sourcing.domain.model.entity.search.Commission
import project.graduation.crowd_sourcing.domain.model.entity.search.SearchHome
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun searchCommissions(
        searchKeyword: String,
        region: String,
        category: String,
        sort: String,
        order: String
    ): Flow<List<Commission>> = flow {
        try {
            println("DEBUG_REPO: API 호출 시작 - 키워드: '$searchKeyword', 카테고리: '$category', 지역: '$region', 정렬: $sort $order")
            
            val response = searchService.getSearchCommission(
                searchKeyword = searchKeyword,
                region = region,
                category = category,
                sort = sort,
                order = order
            )
            
            println("DEBUG_REPO: API 응답 수신 - 상태 코드: ${response.status}, 메시지: ${response.message}, 데이터 크기: ${response.data.size}")
            
            if (response.status == 200) {
                val commissions = response.data.map { it.toCommission() }
                println("DEBUG_REPO: 응답을 도메인 객체로 변환 완료 - 개수: ${commissions.size}")
                
                // 디버깅을 위해 각 커미션 객체 출력
                commissions.forEachIndexed { index, commission ->
                    println("DEBUG_REPO: Commission[$index] - commission: ${commission.commission}, point: ${commission.commissionpoint}")
                }
                
                emit(commissions)
                println("DEBUG_REPO: 데이터 스트림으로 방출 완료")
            } else {
                // 에러 처리
                println("DEBUG_REPO: API 오류 - 상태 코드: ${response.status}, 메시지: ${response.message}")
                emit(emptyList())
                // 필요한 경우 예외 던지기
                // throw Exception("API Error: ${response.message}")
            }
        } catch (e: Exception) {
            // 네트워크 오류 등의 예외 처리
            println("DEBUG_REPO: 예외 발생 - ${e.javaClass.simpleName}: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
            // 필요한 경우 로깅이나 예외 전파
            // throw e
        }
    }

    override suspend fun getSearchHomeInitData(): SearchHome {
        try {
            println("DEBUG_REPO: 초기 검색 데이터 로드 시작")
            
            val response = searchService.getSearchHomeInit()
            
            println("DEBUG_REPO: 초기 검색 데이터 응답 수신 - 상태 코드: ${response.status}, 메시지: ${response.message}")
            
            if (response.status == 200) {
                val searchHomeDto = response.data
                val searchHome = searchHomeDto.toDomain()
                
                println("DEBUG_REPO: 초기 검색 데이터 변환 완료 - 지역: ${searchHome.regionList.size}개, 카테고리: ${searchHome.categoryList.size}개")
                println("DEBUG_REPO: 최근 검색어: ${searchHome.recentKeywords.size}개, 추천 검색어: ${searchHome.recommendedKeywords.size}개")
                
                return searchHome
            } else {
                // 에러 처리 - 빈 데이터 반환
                println("DEBUG_REPO: 초기 검색 데이터 API 오류 - 상태 코드: ${response.status}, 메시지: ${response.message}")
                return SearchHome(
                    regionList = emptyList(),
                    categoryList = emptyList(),
                    recentKeywords = emptyList(),
                    recommendedKeywords = emptyList()
                )
            }
        } catch (e: Exception) {
            // 네트워크 오류 등의 예외 처리
            println("DEBUG_REPO: 초기 검색 데이터 예외 발생 - ${e.javaClass.simpleName}: ${e.message}")
            e.printStackTrace()
            
            // 빈 데이터 반환
            return SearchHome(
                regionList = emptyList(),
                categoryList = emptyList(),
                recentKeywords = emptyList(),
                recommendedKeywords = emptyList()
            )
        }
    }
}