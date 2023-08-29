package give.away.good.deeds.ui.screens.setting.settingsmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.sharePref.service.ProfilePrefService
import kotlinx.coroutines.launch

class SettingViewModel(
    private val authRepository: AuthRepository,
    private val profilePrefService: ProfilePrefService
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
        }
        profilePrefService.clear()
    }
}