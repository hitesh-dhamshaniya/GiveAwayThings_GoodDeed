package give.away.good.deeds.core.dialog

import android.app.Activity
import android.content.Context
import android.view.View
import give.away.good.deeds.R


object DialogFactory {

    fun showErrorDialog(context: Context, messageRes: Int, listener: DialogListener?) {
        DialogHelper(context).apply {
            this.messageRes = messageRes
            resultListener = listener
            positiveButtonText = context.getString(R.string.action_ok)
        }.show(false)
    }

    fun showDeleteDialog(
        context: Context,
        title: String,
        message: String,
        listener: DialogListener?
    ) {
        DialogHelper(context).apply {
            this.title= title
            this.message = message
            resultListener = listener
            positiveButtonText = "Remove"
            negativeButtonText = "Cancel"
        }.show(false)
    }

    fun showErrorDialog(
        context: Context,
        titleRes: String,
        messageRes: String,
        listener: DialogListener? = null
    ) {
        DialogHelper(context).apply {
            title = titleRes
            message = messageRes
            resultListener = listener
            positiveButtonText = context.getString(R.string.action_ok)
        }.show(false)
    }

    fun getProgressDialog(context: Context): DialogHelper {
        return DialogHelper(context).apply {
            viewLayout = R.layout.content_progress_bar
            isTransparent = true
        }
    }

    fun showInfoDialog(context: Context, title: String, message: String) {
        DialogHelper(context).apply {
            this.title = title
            this.message = message
            resultListener = null
            positiveButtonText = context.getString(R.string.action_ok)
        }.show(false)
    }

    fun showInfoDialog(
        context: Context,
        title: String,
        message: String,
        listener: DialogListener?
    ) {
        DialogHelper(context).apply {
            this.title = title
            this.message = message
            resultListener = listener
            positiveButtonText = context.getString(R.string.action_ok)
        }.show(false)
    }

    fun showConfirmDialog(
        context: Context,
        title: String,
        message: String,
        listener: DialogListener?
    ) {
        DialogHelper(context).apply {
            //            this.title = title
            this.message = message
            resultListener = listener
            positiveButtonText = context.getString(R.string.action_yes)
            negativeButtonText = context.getString(R.string.action_no)

        }.show(false)
    }
    fun showConfirmDialog(
        context: Context,
        title: String = "Confirm",
        positiveButton: String,
        negativeButton: String,
        message: String,
        listener: DialogListener?
    ) {
        DialogHelper(context).apply {
            this.title = title
            this.message = message
            resultListener = listener
            positiveButtonText = positiveButton
            negativeButtonText = negativeButton
        }.show(false)
    }

    fun showLoginError(
        activity: Activity,
        error: String,
        clickListener: View.OnClickListener
    ) {
        DialogHelper(activity).apply {
            this.title = "Error"
            this.message = error
            this.resultListener = object : DialogListener {
                override fun onClick(result: Boolean) {
                    if (result) {
                        clickListener.onClick(null)
                    }
                }
            }
            this.positiveButtonText = activity.getString(R.string.action_ok)
            this.negativeButtonText = activity.getString(R.string.action_cancel)
        }.show(false)
    }
}