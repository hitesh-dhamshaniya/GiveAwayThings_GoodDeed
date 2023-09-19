package give.away.good.deeds.ui.screens.authentication.module

import give.away.good.deeds.ui.screens.authentication.forgotpassword.ForgotPasswordViewModel
import give.away.good.deeds.ui.screens.authentication.login.LoginViewModel
import give.away.good.deeds.ui.screens.authentication.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Authentication module
 * @author Hitesh
 * @since 02.09.2023
 */
val authModule = module {
    viewModel {
        LoginViewModel(
            authRepository = get(),
            networkReader = get(),
        )
    }

    viewModel {
        ForgotPasswordViewModel(
            authRepository = get(),
            networkReader = get(),
        )
    }

    viewModel {
        RegisterViewModel(
            authRepository = get(),
            userRepository = get(),
            networkReader = get(),
        )
    }


}