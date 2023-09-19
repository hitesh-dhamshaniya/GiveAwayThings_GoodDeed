package give.away.good.deeds.ui.screens.main.messages.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.ChatGroup
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.ChatRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatGroupViewModel(
    private val networkReader: NetworkReader,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<List<ChatGroup>>>(AppState.Loading)
    val uiState: StateFlow<AppState<List<ChatGroup>>> = _uiState.asStateFlow()

    fun fetchChatGroups() {
        viewModelScope.launch {
            if (!networkReader.isConnected()) {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            val result = chatRepository.getMyChatGroups()
            if (result is CallResult.Success) {
                if (result.data.isEmpty()) {
                    _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
                } else {
                    val sortedList = result.data.sortedByDescending {
                        it.chat?.timestamp
                    }
                    _uiState.emit(AppState.Result(sortedList))
                }
            } else {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
            }
        }
    }
}