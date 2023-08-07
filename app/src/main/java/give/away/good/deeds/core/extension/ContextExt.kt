package give.away.good.deeds.core.extension

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat

val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

fun Context.getColorRes(color: Int) = ContextCompat.getColor(this, color)

fun View.getColorRes(color: Int) = ContextCompat.getColor(context, color)

fun Context.getColorStateListRes(color: Int) = ColorStateList.valueOf(getColorRes(color))

fun View.getColorStateListRes(color: Int) = ColorStateList.valueOf(getColorRes(color))

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}