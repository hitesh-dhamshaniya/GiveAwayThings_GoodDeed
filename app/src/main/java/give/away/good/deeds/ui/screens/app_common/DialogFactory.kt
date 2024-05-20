package give.away.good.deeds.ui.screens.app_common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import give.away.good.deeds.R


object DialogFactory {

    fun showErrorDialog(
        context: Context,
        title: String,
        message: String,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.action_ok, null)
            .show()
    }

    fun showInfoDialog(
        context: Context,
        title: String,
        message: String,
        listener: (() -> Unit)?
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.action_ok) { _, _ ->
                listener?.invoke()
            }
            .show()
    }

}