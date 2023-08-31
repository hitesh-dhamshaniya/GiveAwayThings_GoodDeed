package give.away.good.deeds.ui.screens.setting.changepassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var changePswrdSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    fun changePassword(
        currentPassword: String,
        newPassword: String
    ) {
        viewModelScope.launch {
            when (val result = authRepository.changePassword(currentPassword, newPassword)) {
                is CallResult.Success -> {
                    changePswrdSuccess = true
                }

                is CallResult.Failure -> {
                    errorMessage = result.message ?: ""
                }
            }
        }
    }

}