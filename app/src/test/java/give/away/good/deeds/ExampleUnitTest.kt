package give.away.good.deeds

import give.away.good.deeds.core.extension.isEmailValid
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun isValid_emailAddress(){
        assertFalse("".isEmailValid())

        assertFalse("hitesh".isEmailValid())

        assertFalse("hitesh@".isEmailValid())

        assertFalse("hitesh@yoo".isEmailValid())
    }
}