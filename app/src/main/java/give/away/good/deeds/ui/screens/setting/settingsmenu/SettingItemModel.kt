package give.away.good.deeds.ui.screens.setting.settingsmenu

import give.away.good.deeds.R

data class SettingItemModel(
    val icon: Int,
    val title: String,
    val route: String
)

object SettingUtil {
    private val settingItemsList: MutableList<SettingItemModel> = mutableListOf()
    const val PROFILE = "Profile"
    const val CHANGE_PASSWORD = "Change Password"
    const val SET_LOCATION = "Location"
    const val MY_LISTING = "My Posts"
    const val LOGOUT = "Logout"

    private fun addItems() {
        settingItemsList.add(SettingItemModel(R.drawable.ic_profile, PROFILE, "profile"))
        settingItemsList.add(SettingItemModel(R.drawable.ic_change_password, CHANGE_PASSWORD, "change_password"))
        settingItemsList.add(SettingItemModel(R.drawable.ic_location, SET_LOCATION, "location"))
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, MY_LISTING, "account_listing"))
        settingItemsList.add(SettingItemModel(R.drawable.ic_logout, LOGOUT, "logout"))
    }

    fun getSettingItems(): MutableList<SettingItemModel> {
        if (settingItemsList.isEmpty()) {
            addItems()
        }
        return settingItemsList
    }
}