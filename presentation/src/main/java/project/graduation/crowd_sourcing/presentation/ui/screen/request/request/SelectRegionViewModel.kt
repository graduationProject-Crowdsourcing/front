package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import javax.inject.Inject

@HiltViewModel
class SelectRegionViewModel @Inject constructor(
    private val martSearchUseCase: MartSearchUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 선택된 지역 리스트
    private val _selectedRegions = MutableStateFlow<List<String>>(emptyList())
    val selectedRegions: StateFlow<List<String>> = _selectedRegions.asStateFlow()

    // 선택된 마트 리스트 (마트명만)
    private val _selectedMarts = MutableStateFlow<List<String>>(emptyList())
    val selectedMarts: StateFlow<List<String>> = _selectedMarts.asStateFlow()

    // 현재 지역에 따른 마트 리스트
    private val _martList = MutableStateFlow<List<MartEntity>>(emptyList())
    val martList: StateFlow<List<MartEntity>> = _martList.asStateFlow()

    init {
        val prefillRegion = savedStateHandle.get<String>("selectedRegion")
        val prefillMarts = savedStateHandle.get<List<String>>("selectedMarts_prefill")

        _selectedRegions.value = listOfNotNull(prefillRegion)
        _selectedMarts.value = prefillMarts ?: emptyList()

        prefillRegion?.let { region ->
            // ✅ 지역 선택 초기화 시 마트도 같이 불러오게!
            onRegionToggle(region)
        }
    }


    fun prefillSelections(regions: List<String>, marts: List<String>) {
        _selectedRegions.value = regions
        _selectedMarts.value = marts

        if (regions.isNotEmpty()) {
            viewModelScope.launch {
                val allMarts = regions.flatMap { region ->
                    try {
                        martSearchUseCase.getMartList(region).getOrDefault(emptyList())
                    } catch (e: Exception) {
                        emptyList()
                    }
                }.distinctBy { it.martName }

                _martList.value = allMarts

                _selectedMarts.value = allMarts
                    .filter { marts.contains(it.martName) }
                    .map { it.martName }
            }
        }
    }


    // 지역 선택 시 호출
    fun onRegionToggle(region: String) {
        // 지역 단일 선택
        _selectedRegions.value = listOf(region)

        // region 선택 시 해당 마트 불러오기
        viewModelScope.launch {
            val martList = try {
                martSearchUseCase.getMartList(region)
                    .onFailure { e ->
                        Log.e("🔥마트", "getMartList 실패: ${e.message}")
                    }
                    .getOrDefault(emptyList())
            } catch (e: Exception) {
                Log.e("🔥마트", "예외 발생: ${e.message}")
                emptyList()
            }

            Log.d("🔥마트", "받은 마트 개수: ${martList.size}")
            _martList.value = martList.distinctBy { it.martName }
        }


        _selectedMarts.value = emptyList() // 선택 초기화
    }

    // 지역 복수 선택 시...
//        val current = _selectedRegions.value.toMutableList()
//        if (current.contains(region)) {
//            current.remove(region)
//        } else {
//            current.add(region)
//        }
//        _selectedRegions.value = current
//
//        // 여러 지역구 마트 병합해서 불러오기
//        if (current.isNotEmpty()) {
//            viewModelScope.launch {
//                val allMarts = current.flatMap { region ->
//                    try {
//                        martSearchUseCase.getMartList(region).getOrDefault(emptyList())
//                    } catch (e: Exception) {
//                        emptyList()
//                    }
//                }
//                _martList.value = allMarts.distinctBy { it.martName }
//            }
//        } else {
//            _martList.value = emptyList()
//        }


    // 마트 선택 시 호출
    fun onMartToggle(martName: String) {
        val current = _selectedMarts.value.toMutableList()
        if (current.contains(martName)) {
            current.remove(martName)
        } else {
            current.add(martName)
        }
        _selectedMarts.value = current
    }
}
