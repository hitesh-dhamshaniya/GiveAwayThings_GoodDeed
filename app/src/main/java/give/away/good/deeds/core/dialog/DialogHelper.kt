package give.away.good.deeds.core.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import give.away.good.deeds.R

class DialogHelper(private val context: Context) {

    var isTransparent: Boolean = false

    var title: String? = null
    var titleRes: Int = 0

    var message: String? = null
    var messageRes: Int = 0

    var cancelable: Boolean = false

    var positiveButtonText: String? = null
    var positiveButtonTextRes: Int = 0
    var resultListener: DialogListener? = null

    var negativeButtonText: String? = null
    var negativeButtonTextRes: Int = 0

    var viewLayout: Int = 0
    var view: View? = null

    var alertDialog: AlertDialog? = null

    fun show(forceWrap: Boolean = true) {
        val builder = getAlertDialogBuilder(forceWrap).apply {
            setCancelable(cancelable)
            if (title != null) {
                setTitle(title)
            }
            if (titleRes != 0) {
                setTitle(titleRes)
            }
            if (message != null) {
                setMessage(message)
            }
            if (messageRes != 0) {
                setMessage(messageRes)
            }
        }

        if (positiveButtonTextRes != 0) {
            builder.setPositiveButton(positiveButtonTextRes, positiveClickListener)
        } else if (positiveButtonText != null) {
            builder.setPositiveButton(positiveButtonText, positiveClickListener)
        }

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, negativeClickListener)
        } else if (negativeButtonTextRes != 0) {
            builder.setNegativeButton(negativeButtonTextRes, negativeClickListener)
        }

        if (view?.parent != null) {
            (view?.parent as ViewGroup).removeView(view)
        }

        if (view != null) {
            builder.setView(view)
        } else if (viewLayout != 0) {
            view = LayoutInflater.from(context).inflate(viewLayout, null)
            builder.setView(view)
        }

        alertDialog = builder.create()

        alertDialog?.show()
        if (isTransparent) {
            alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun getAlertDialogBuilder(forceWrap: Boolean): AlertDialog.Builder {
        return if (forceWrap) AlertDialog.Builder(context, R.style.NarrowDialog)
        else AlertDialog.Builder(context)
    }

    fun hide() {
        alertDialog?.cancel()
    }

    val positiveClickListener = DialogInterface.OnClickListener { dialog, which -> resultListener?.onClick(true) }
    val negativeClickListener = DialogInterface.OnClickListener { dialog, which -> resultListener?.onClick(false) }
}

interface DialogListener {
    fun onClick(result: Boolean)
}