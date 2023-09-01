package give.away.good.deeds.ui.screens.main.setting.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.ui.screens.main.setting.common.SettingState
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingState<Unit>>(SettingState.None)
    val uiState: StateFlow<SettingState<Unit>> = _uiState.asStateFlow()

    fun changePassword(
        currentPassword: String,
        newPassword: String
    ) {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(SettingState.NoInternet)
                return@launch
            }

            _uiState.emit(SettingState.Loading)
            when (val result = authRepository.changePassword(currentPassword, newPassword)) {
                is CallResult.Success -> {
                    _uiState.emit(SettingState.Result())
                }

                is CallResult.Failure -> {
                    _uiState.emit(SettingState.Error(result.message ?: ""))
                }
            }
        }
    }

    fun resetState() {
        viewModelScope.launch {
            _uiState.emit(SettingState.None)
        }
    }

}