package give.away.good.deeds.ui.screens.state

sealed class AppState<out T : Any> {
    data object Ideal : AppState<Nothing>()

    data object Loading : AppState<Nothing>()

    class Result<out T : Any>(val data: T? = null) : AppState<T>()

    class Error(
        val message: String = "",
        val cause: ErrorCause = ErrorCause.UNKNOWN
    ) : AppState<Nothing>()

}

enum class ErrorCause {
    UNKNOWN,
    NO_INTERNET,
    NO_RESULT,
    SESSION_TIMEOUT
}