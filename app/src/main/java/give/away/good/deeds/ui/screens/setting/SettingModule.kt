package give.away.good.deeds.ui.screens.setting

import give.away.good.deeds.ui.screens.setting.changepassword.ChangePasswordViewModel
import give.away.good.deeds.ui.screens.setting.profile.ProfileViewModel
import give.away.good.deeds.ui.screens.setting.settingsmenu.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingModule = module {
    viewModel {
        SettingViewModel(
            authRepository = get(),
            profilePrefService = get()
        )
    }
    viewModel {
        ChangePasswordViewModel(
            authRepository = get()
        )
    }
    viewModel {
        ProfileViewModel(
            userRepository = get(),
            mediaRepository = get()
        )
    }
}