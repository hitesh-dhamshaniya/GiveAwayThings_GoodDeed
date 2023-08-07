package give.away.good.deeds.core.extension

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File


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

@SuppressLint("CheckResult")
fun ImageView.setRemoteImage(url: String, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(url)
    glide.error(androidx.core.R.drawable.ic_call_answer)
    glide.placeholder(androidx.core.R.drawable.ic_call_answer) //Place Holder
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }

}

fun ImageView.setLocalImage(file: File, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(file)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }
}
