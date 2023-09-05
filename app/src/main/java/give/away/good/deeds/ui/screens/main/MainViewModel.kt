package give.away.good.deeds.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import give.away.good.deeds.repository.UserConfigRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userConfigRepository: UserConfigRepository
) : ViewModel() {

    fun saveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { result ->
            if(result.isSuccessful){
                viewModelScope.launch {
                    userConfigRepository.addToken(result.result)
                }
            }
        }
    }

}