package give.away.good.deeds.ui.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _eventChannel = Channel<AuthenticationState<Boolean>>(Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    /**
     * TO login API call
     * @param email: String
     * @param password: String
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _eventChannel.send(AuthenticationState.Loading)
            if(!networkReader.isConnected()){
                _eventChannel.send(AuthenticationState.NoInternet)
                return@launch
            }
            when (authRepository.login(email = email, password = password)) {
                is CallResult.Success -> {
                    _eventChannel.send(AuthenticationState.Result())
                }

                is CallResult.Failure -> {
                    _eventChannel.send(AuthenticationState.Error())
                }
            }
        }
    }


}