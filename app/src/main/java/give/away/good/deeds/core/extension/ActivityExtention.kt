package give.away.good.deeds.core.extension

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import give.away.good.deeds.R
import java.util.*


fun Activity.launchActivity(
    cls: Class<out Activity>,
    bundle: Bundle = Bundle(),
    clearStack: Boolean = false
) {
    val intent = Intent(this, cls)
    intent.putExtras(bundle)

    val animBundle = getAnimationBundle()

    startActivity(intent, animBundle)

    if (clearStack) {
        finishAffinity()
    }
}

fun Activity.launchActivityForResult(
    cls: Class<out Activity>,
    bundle: Bundle = Bundle(),
    reqCode: Int = 2404,
    clearStack: Boolean = false
) {
    val intent = Intent(this, cls)
    intent.putExtras(bundle)

    val animBundle = getAnimationBundle()

    startActivityForResult(intent, reqCode, animBundle)

    if (clearStack) {
        finishAffinity()
    }
}

fun Activity.launchActivity(
    intent: Intent,
    bundle: Bundle = Bundle(),
    clearStack: Boolean = false
) {
    intent.putExtras(bundle)

    val animBundle = getAnimationBundle()

    startActivity(intent, animBundle)

    if (clearStack) {
        finishAffinity()
    }
}

fun Activity.getAnimationBundle(): Bundle =
    ActivityOptions
        .makeCustomAnimation(
            this,
            R.anim.activity_slide_in_1,
            R.anim.activity_slide_in_2
        )
        .toBundle()

fun overridePendingTransition(activity: Activity) {
    activity.overridePendingTransition(
        R.anim.activity_slide_from_left,
        R.anim.activity_slide_to_right
    )
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Activity.showSnackbar(snackbarText: String, timeLength: Int) {
    //Snackbar.make(findViewById<View>(android.R.id.content), snackbarText, timeLength).show()
}

fun setFullScreenMode(activity: Activity) {

    val w = activity.window // in Activity's onCreate() for instance
    //make full transparent statusBar
    if (Build.VERSION.SDK_INT in 19..20) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    w.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        w.statusBarColor = Color.TRANSPARENT
    }
}

private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun setStatusBarIconTheme(activity: Activity, shouldChangeStatusBarTintToDark: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decor = activity.getWindow().getDecorView()
        if (shouldChangeStatusBarTintToDark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.setSystemUiVisibility(0)
        }
    }
}

fun Activity.setLanguage(language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = resources.configuration
    config.setLocale(locale)

    resources.updateConfiguration(config, resources.displayMetrics)
}

fun Activity.hideKeyboard() {
    val view: View? = findViewById(android.R.id.content)
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.getVersionName(): String = try {
    "App version ${getPackageInfo().versionName}"
} catch (e: PackageManager.NameNotFoundException) {
    ""
}

@Suppress("DEPRECATION")
fun Context.getPackageInfo(): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
}