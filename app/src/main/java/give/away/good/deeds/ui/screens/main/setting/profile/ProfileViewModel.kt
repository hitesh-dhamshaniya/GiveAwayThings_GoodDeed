package give.away.good.deeds.ui.screens.main.setting.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.MediaRepository
import give.away.good.deeds.repository.UserRepository
import give.away.good.deeds.ui.screens.main.setting.common.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingState<Unit>>(SettingState.None)
    val uiState: StateFlow<SettingState<Unit>> = _uiState.asStateFlow()

    private val defaultErrorMessage = "Something went wrong. Please try again!"

    val formState = mutableStateMapOf<String, String>()

    fun fetchUser() {
        viewModelScope.launch {
            _uiState.emit(SettingState.Loading)
            val result = userRepository.getUser()
            if (result is CallResult.Success) {
                result.data.apply {
                    formState.putAll(this.toMap())
                }
            }
            _uiState.emit(SettingState.None)
        }
    }

    fun updateProfile(
        context: Context,
        userId: String,
        firstName: String,
        lastName: String,
        profilePic: String?
    ) {

        viewModelScope.launch {
            _uiState.emit(SettingState.Loading)
            val updates = mutableMapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
            )

            try {
                if (!profilePic.isNullOrBlank() && !profilePic.startsWith("http")) {
                    mediaRepository.uploadProfileImage(
                        context, userId, Uri.parse(profilePic)
                    ).let { url ->
                        updates["profilePic"] = url
                    }
                }

                val result = userRepository.updateUser(userId, updates)
                if (result is CallResult.Success) {
                    _uiState.emit(SettingState.Result())
                } else if (result is CallResult.Failure) {
                    _uiState.emit(SettingState.Error(result.message ?: defaultErrorMessage))
                }
            } catch (ex: Exception) {
                _uiState.emit(SettingState.Error(ex.message ?: defaultErrorMessage))
            }
        }
    }


}