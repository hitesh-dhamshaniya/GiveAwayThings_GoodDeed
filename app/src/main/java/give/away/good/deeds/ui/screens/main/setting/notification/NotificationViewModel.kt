package give.away.good.deeds.ui.screens.main.setting.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.NotificationRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<List<Notification>>>(AppState.Loading)
    val uiState: StateFlow<AppState<List<Notification>>> = _uiState.asStateFlow()

    fun fetchNotifications() {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            val result = repository.getNotifications()
            if (result is CallResult.Success) {
                if (result.data.isNotEmpty()) {
                    val sortedList = result.data.sortedByDescending {
                        it.time
                    }
                    _uiState.emit(AppState.Result(sortedList))
                } else {
                    _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
                }
            } else {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
            }
        }
    }

}