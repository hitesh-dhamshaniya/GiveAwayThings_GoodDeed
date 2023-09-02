package give.away.good.deeds.ui.screens.main.setting.profile

import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.MediaRepository
import give.away.good.deeds.repository.UserRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<Unit>>(AppState.Ideal)
    val uiState: StateFlow<AppState<Unit>> = _uiState.asStateFlow()

    private val defaultErrorMessage = "Something went wrong. Please try again!"

    val formState = mutableStateMapOf<String, String>()

    fun fetchUser() {
        viewModelScope.launch {
            _uiState.emit(AppState.Loading)
            val result = userRepository.getUser()
            if (result is CallResult.Success) {
                result.data.apply {
                    formState.putAll(this.toMap())
                }
            }
            _uiState.emit(AppState.Ideal)
        }
    }

    fun updateProfile(
        userId: String,
        firstName: String,
        lastName: String,
        profilePic: String?
    ) {

        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            val updates = mutableMapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
            )

            try {
                if (!profilePic.isNullOrBlank() && !profilePic.startsWith("http")) {
                    mediaRepository.uploadProfileImage(userId, Uri.parse(profilePic)).let { url ->
                        updates["profilePic"] = url
                    }
                }

                val result = userRepository.updateUser(userId, updates)
                if (result is CallResult.Success) {
                    _uiState.emit(AppState.Result())
                } else if (result is CallResult.Failure) {
                    _uiState.emit(AppState.Error(message = result.message ?: defaultErrorMessage))
                }
            } catch (ex: Exception) {
                _uiState.emit(AppState.Error(message = ex.message ?: defaultErrorMessage))
            }
        }
    }


}