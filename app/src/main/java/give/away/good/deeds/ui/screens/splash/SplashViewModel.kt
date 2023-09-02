package give.away.good.deeds.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.BuildConfig
import give.away.good.deeds.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val mSplashDelay = 2500L

    private val _event = Channel<Boolean>()
    val event: Flow<Boolean> = _event.receiveAsFlow()

    fun preProcessing() {
        viewModelScope.launch {
            if (!BuildConfig.DEBUG)
                delay(mSplashDelay)
            _event.send(authRepository.isLoggedIn())
        }
    }

}