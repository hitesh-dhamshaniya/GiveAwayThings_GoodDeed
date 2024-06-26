package give.away.good.deeds.di

import give.away.good.deeds.sharePref.di.sharedPrefModules
import give.away.good.deeds.ui.screens.main.post.postModule
import give.away.good.deeds.ui.screens.main.setting.settingModule

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val appComponent = listOf(
    appViewModules,
    sharedPrefModules,
    settingModule,
    postModule
)