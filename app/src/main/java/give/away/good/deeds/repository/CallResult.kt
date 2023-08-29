package give.away.good.deeds.repository

sealed class CallResult<out T : Any> {

    class Success<out T : Any>(val data: T) : CallResult<T>()

    class Failure(val message: String?) : CallResult<Nothing>()

}
