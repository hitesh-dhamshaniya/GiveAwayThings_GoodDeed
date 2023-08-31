package give.away.good.deeds.ui.screens.setting.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.MediaRepository
import give.away.good.deeds.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val defaultErrorMessage = "Something went wrong. Please try again!"

    var updateSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    val formState = mutableStateMapOf<String, String>()

    fun fetchUser() {
        viewModelScope.launch {
            val result = userRepository.getUser()
            if (result is CallResult.Success) {
                result.data.apply {
                    formState.putAll(this.toMap())
                }
            }
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