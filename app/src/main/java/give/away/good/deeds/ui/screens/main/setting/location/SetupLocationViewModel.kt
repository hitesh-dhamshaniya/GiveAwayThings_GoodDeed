package give.away.good.deeds.ui.screens.main.setting.location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import give.away.good.deeds.AppConstant
import give.away.good.deeds.network.model.UserConfig
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.UserConfigRepository
import give.away.good.deeds.utils.LocationUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetupLocationViewModel(
    private val context: Context,
    private val userConfigRepository: UserConfigRepository,
) : ViewModel() {

    private val defaultConfig = UserConfig(
        latLng = AppConstant.defaultLocation,
        address = AppConstant.defaultAddress
    )

    private val _uiState = MutableStateFlow<UserConfig?>(null)
    val uiState: StateFlow<UserConfig?> = _uiState.asStateFlow()

    fun fetchLocation() {
        viewModelScope.launch {
            val result = userConfigRepository.getLocation()
            if (result is CallResult.Success) {
                _uiState.emit(result.data)
            } else {
                _uiState.emit(defaultConfig)
            }
        }
    }

    fun setTempLocation(latLng: LatLng) {
        viewModelScope.launch {
            val address = LocationUtil.getAddress(context, latLng)
            _uiState.emit(UserConfig(
                latLng = latLng,
                address = address ?: ""
            ))
        }
    }

    fun setLocation() {
        viewModelScope.launch {
            userConfigRepository.setLocation(_uiState.value ?: defaultConfig)
        }
    }

}