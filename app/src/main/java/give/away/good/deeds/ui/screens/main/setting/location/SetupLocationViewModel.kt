package give.away.good.deeds.ui.screens.main.setting.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.UserConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetupLocationViewModel(
    private val userConfigRepository: UserConfigRepository,
) : ViewModel() {

    val defaultLatLng = LatLng(51.509865, -0.118092)

    private val _uiState = MutableStateFlow<LatLng?>(null)
    val uiState: StateFlow<LatLng?> = _uiState.asStateFlow()

    fun getLocation() {
        viewModelScope.launch {
            val result = userConfigRepository.getLocation()
            if (result is CallResult.Success) {
                _uiState.emit(result.data)
            } else {
                _uiState.emit(defaultLatLng)
            }
        }
    }

    fun setTempLocation(latLng: LatLng) {
        viewModelScope.launch {
            _uiState.emit(latLng)
        }
    }

    fun setLocation() {
        viewModelScope.launch {
            userConfigRepository.setLocation(_uiState.value ?: defaultLatLng)
        }
    }

}