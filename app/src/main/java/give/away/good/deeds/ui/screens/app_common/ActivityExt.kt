package give.away.good.deeds.ui.screens.app_common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import kotlin.reflect.KClass

/**
 * Activity Extension functions
 * @author Hitesh
 * @since 20.08.2023
 */
fun Activity.launchActivity(cls: KClass<out Activity>) {
    startActivity(Intent(this, cls.java))
}

fun Activity.hideSoftKeyboard() {
    currentFocus?.let { input ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(input.windowToken, 0)
    }
}

