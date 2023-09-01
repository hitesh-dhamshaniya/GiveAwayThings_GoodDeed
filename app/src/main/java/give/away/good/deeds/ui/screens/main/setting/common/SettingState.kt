package give.away.good.deeds.ui.screens.main.setting.common

sealed class SettingState<out T: Any> {
    data object Loading : SettingState<Nothing>()

    data object NoInternet : SettingState<Nothing>()

    class Result<out T: Any>(val data: T? = null) : SettingState<T>()

    class Error(
        val message: String = ""
    ) : SettingState<Nothing>()

    data object None : SettingState<Nothing>()
}