package give.away.good.deeds.di

import give.away.good.deeds.sharePref.di.sharedPrefModules

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val appComponent = listOf(
    appViewModules,
    sharedPrefModules,
    /*landingViewModules,
    domainVariables*/
)