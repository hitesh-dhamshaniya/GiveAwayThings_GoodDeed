package give.away.good.deeds.ui.screens.setting.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.User
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.MediaRepository
import give.away.good.deeds.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository, private val mediaRepository: MediaRepository
) : ViewModel() {

    private val defaultErrorMessage = "Something went wrong. Please try again!"

    var updateSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    fun fetchUser() {
        viewModelScope.launch {
            val result = userRepository.getUser()
            if (result is CallResult.Success) {
                _uiState.emit(result.data)
            }
        }
    }

    fun updateProfile(context: Context, user: User) {

        viewModelScope.launch {
            val updates = mutableMapOf<String, Any>(
                "firstName" to (user.firstName ?: ""),
                "lastName" to (user.lastName ?: ""),
            )

            try {
                if (user.profilePic != null && user.profilePic?.startsWith("http") == false) {
                    mediaRepository.uploadProfileImage(
                        context, user.id ?: "", Uri.parse(user.profilePic)
                    )?.let { url ->
                        updates["profilePic"] = url
                    }
                }

                val result = userRepository.updateUser(user.id ?: "", updates)
                if (result is CallResult.Success) {
                    updateSuccess = true
                } else if (result is CallResult.Failure) {
                    errorMessage = result.message ?: ""
                }
            } catch (ex: Exception) {
                errorMessage = ex.message ?: defaultErrorMessage
            }
        }
    }


}