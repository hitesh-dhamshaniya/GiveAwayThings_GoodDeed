package give.away.good.deeds.ui.screens.main.setting

import give.away.good.deeds.ui.screens.main.setting.changepassword.ChangePasswordViewModel
import give.away.good.deeds.ui.screens.main.setting.location.SetupLocationViewModel
import give.away.good.deeds.ui.screens.main.setting.profile.ProfileViewModel
import give.away.good.deeds.ui.screens.main.setting.menu.SettingViewModel
import give.away.good.deeds.ui.screens.main.setting.notification.NotificationViewModel
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
            authRepository = get(),
            networkReader = get(),
        )
    }
    viewModel {
        ProfileViewModel(
            userRepository = get(),
            mediaRepository = get(),
            networkReader = get(),
        )
    }
    viewModel {
        SetupLocationViewModel(
            context = get(),
            userConfigRepository = get(),
        )
    }
    viewModel {
        NotificationViewModel(
            networkReader = get(),
            repository = get(),
        )
    }
}