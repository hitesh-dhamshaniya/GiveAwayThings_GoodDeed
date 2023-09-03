package give.away.good.deeds.ui.screens.authentication.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    val uiState = MutableLiveData<AuthenticationState<Boolean>>(AuthenticationState.None)

    /**
     * Call forgot password API, send reset password link to email
     * @param email: String
     */
    fun forgotPassword(email: String) {
        uiState.postValue(AuthenticationState.Loading)
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                uiState.postValue(AuthenticationState.NoInternet)
                return@launch
            }
            when (authRepository.forgotPassword(email)) {
                is CallResult.Success -> {
                    uiState.postValue(AuthenticationState.Result())
                }
                is CallResult.Failure -> {
                    uiState.postValue(AuthenticationState.Error())
                }
            }
        }
    }
}