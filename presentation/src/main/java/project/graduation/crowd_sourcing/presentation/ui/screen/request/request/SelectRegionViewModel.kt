package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

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
        val prefillRegions = savedStateHandle.get<List<String>>("selectedRegions_prefill")
        val prefillMarts = savedStateHandle.get<List<String>>("selectedMarts_prefill")

        _selectedRegions.value = prefillRegions ?: emptyList()
        _selectedMarts.value = prefillMarts ?: emptyList() // 💡 유지

        // 마트도 초기 로딩
        prefillRegions?.let { regions ->
            viewModelScope.launch {
                val allMarts = regions.flatMap { region ->
                    try {
                        martSearchUseCase.getMartList(region).getOrDefault(emptyList())
                    } catch (e: Exception) {
                        emptyList()
                    }
                }.distinctBy { it.martName }

                _martList.value = allMarts

                // 마트 리스트 로드 후 선택 상태 다시 반영
                prefillMarts?.let { selected ->
                    _selectedMarts.value = allMarts
                        .filter { selected.contains(it.martName) }
                        .map { it.martName }
                }
            }
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
        val current = _selectedRegions.value.toMutableList()
        if (current.contains(region)) {
            current.remove(region)
        } else {
            current.add(region)
        }
        _selectedRegions.value = current

        // 여러 지역구 마트 병합해서 불러오기
        if (current.isNotEmpty()) {
            viewModelScope.launch {
                val allMarts = current.flatMap { region ->
                    try {
                        martSearchUseCase.getMartList(region).getOrDefault(emptyList())
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
                _martList.value = allMarts.distinctBy { it.martName }
            }
        } else {
            _martList.value = emptyList()
        }

        // 마트 선택 초기화 (지역 바뀌었을 수 있으므로)
        _selectedMarts.value = emptyList()
    }

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
