package give.away.good.deeds.ui.screens.app_common

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText

fun EditText.getInputString() = text.toString().trim()

fun EditText.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(getInputString()) && Patterns.EMAIL_ADDRESS.matcher(getInputString()).matches()
}

