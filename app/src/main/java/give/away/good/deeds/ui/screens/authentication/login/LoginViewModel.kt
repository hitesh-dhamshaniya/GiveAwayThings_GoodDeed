package give.away.good.deeds.ui.screens.authentication.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState = MutableLiveData<AuthenticationState<Boolean>>(AuthenticationState.None)

    /**
     * TO login API call
     * @param email: String
     * @param password: String
     */
    fun login(email: String, password: String) {
        uiState.postValue(AuthenticationState.Loading)
        viewModelScope.launch {
            when (authRepository.login(email = email, password = password)) {
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