package give.away.good.deeds.repository

/**
 * @author Hitesh
 * @since 02.09.2023
 */
sealed class CallResult<out T : Any> {

    class Success<out T : Any>(val data: T) : CallResult<T>()

    class Failure(val message: String?) : CallResult<Nothing>()

}
