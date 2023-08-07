package give.away.good.deeds.network.model

import give.away.good.deeds.R

data class SettingItemModel(
    val icon: Int,
    val title: String
)

object SettingUtil {
    private val settingItemsList: MutableList<SettingItemModel> = mutableListOf()
    const val PROFILE = "Profile"
    const val CHANGE_PASSWORD = "Change Password"
    const val SET_LOCATION = "Location"
    const val MY_LISTING = "My Listing"
    const val LOGOUT = "Logout"

    private fun addItems() {
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, PROFILE))
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, CHANGE_PASSWORD))
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, SET_LOCATION))
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, MY_LISTING))
        settingItemsList.add(SettingItemModel(R.drawable.ic_home, LOGOUT))
    }

    fun getSettingItems(): MutableList<SettingItemModel> {
        if (settingItemsList.isEmpty()) {
            addItems()
        }
        return settingItemsList
    }
}