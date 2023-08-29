package give.away.good.deeds.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val mSplashDelay = 1500L

    private val _event = Channel<Boolean>()
    val event: Flow<Boolean> = _event.receiveAsFlow()

    fun preProcessing() {
        viewModelScope.launch {
            delay(mSplashDelay)
            _event.send(auth.currentUser != null)
        }
    }

}