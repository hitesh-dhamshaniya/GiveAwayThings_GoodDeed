package give.away.good.deeds.ui.screens.app_common

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun Fragment.startActivity(cls: KClass<out Activity>) {
    val intent = Intent(requireActivity(), cls.java)
    startActivity(intent)
}

fun Fragment.hideSoftKeyboard() {
    requireActivity().hideSoftKeyboard()
}