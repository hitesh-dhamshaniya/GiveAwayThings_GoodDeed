package give.away.good.deeds.ui.screens.authentication.common

sealed class AuthenticationState<out T: Any> {
    data object Loading : AuthenticationState<Nothing>()

    data object NoInternet : AuthenticationState<Nothing>()

    class Result<out T: Any>(val data: T? = null) : AuthenticationState<T>()

    class Error(
        val message: String = ""
    ) : AuthenticationState<Nothing>()

    data object None : AuthenticationState<Nothing>()
}