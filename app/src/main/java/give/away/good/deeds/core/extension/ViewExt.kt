package give.away.good.deeds.core.extension

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText


fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun EditText.getInputString() = text.toString().trim()

fun EditText.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(getInputString()) && Patterns.EMAIL_ADDRESS.matcher(getInputString()).matches()
}

