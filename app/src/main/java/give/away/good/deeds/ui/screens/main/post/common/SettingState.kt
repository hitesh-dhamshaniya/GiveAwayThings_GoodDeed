package give.away.good.deeds.ui.screens.main.post.common

sealed class PostState<out T: Any> {
    data object Loading : PostState<Nothing>()

    data object NoInternet : PostState<Nothing>()

    data object Empty : PostState<Nothing>()

    class Result<out T: Any>(val data: T? = null) : PostState<T>()

    class Error(
        val message: String = ""
    ) : PostState<Nothing>()

    data object None : PostState<Nothing>()
}