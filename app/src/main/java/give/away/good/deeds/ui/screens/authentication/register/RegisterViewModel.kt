package give.away.good.deeds.ui.screens.authentication.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.UserRepository
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState = MutableLiveData<AuthenticationState<Boolean>>(AuthenticationState.None)

    companion object {
        const val TAG: String = "RegisterViewModel"
    }

    /**
     * @param email: String
     * @param password: String
     */
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        uiState.postValue(AuthenticationState.Loading)
        viewModelScope.launch {
            when (val result = authRepository.register(email = email, password = password)) {
                is CallResult.Success -> {
                    saveUser(result.data, firstName, lastName, email)
                }

                is CallResult.Failure -> {
                    uiState.postValue(AuthenticationState.Error(message = result.message ?: ""))
                }
            }
        }
    }


    /**
     * Save user details to Firestore
     * @param userId: String
     * @param firstName: String
     * @param lastName: String
     * @param email: String
     */
    private suspend fun saveUser(
        userId: String,
        firstName: String,
        lastName: String,
        email: String
    ) {
        when (val result = userRepository.createUser(userId, firstName, lastName, email)) {
            is CallResult.Success -> {
                uiState.postValue(AuthenticationState.Result())
            }
            is CallResult.Failure -> {
                uiState.postValue(AuthenticationState.Error(result.message ?: ""))
            }
        }
    }

}