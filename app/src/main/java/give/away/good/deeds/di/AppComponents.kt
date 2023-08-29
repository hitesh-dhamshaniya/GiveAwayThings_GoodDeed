package give.away.good.deeds.di

import give.away.good.deeds.ui.screens.setting.settingModule
import give.away.good.deeds.sharePref.di.sharedPrefModules

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val appComponent = listOf(
    appViewModules,
    sharedPrefModules,
    settingModule,
    /*landingViewModules,
    domainVariables*/
)