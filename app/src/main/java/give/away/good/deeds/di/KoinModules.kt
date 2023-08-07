package give.away.good.deeds.di


import give.away.good.deeds.screens.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val appViewModules = module {
    viewModel { SplashViewModel(get()) }
}

/*val landingViewModules = module {
    viewModel {
        LandingViewModel(get())
    }
}

val domainVariables = module {
    single(named("MEDIA_URL")) {
        BuildConfig.BASE_URL + BuildConfig.MEDIA_PATH
    }*/