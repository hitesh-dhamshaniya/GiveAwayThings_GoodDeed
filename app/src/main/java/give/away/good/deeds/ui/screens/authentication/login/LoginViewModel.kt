package give.away.good.deeds.ui.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthenticationState<Boolean>>(AuthenticationState.None)
    val uiState: StateFlow<AuthenticationState<Boolean>> = _uiState.asStateFlow()

    /**
     * TO login API call
     * @param email: String
     * @param password: String
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.emit(AuthenticationState.Loading)
            when (authRepository.login(email = email, password = password)) {
                is CallResult.Success -> {
                    _uiState.emit(AuthenticationState.Result())
                }

                is CallResult.Failure -> {
                    _uiState.emit(AuthenticationState.Error())
                }
            }
        }
    }


}