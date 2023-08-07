package give.away.good.deeds.core.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import give.away.good.deeds.R
import give.away.good.deeds.core.dialog.DialogFactory
import give.away.good.deeds.core.dialog.DialogHelper

abstract class BaseActivity<VDB : ViewDataBinding, VM : ViewModel>() : AppCompatActivity() {
    protected lateinit var mActivity: Activity
    protected lateinit var mViewModel: VM
    protected lateinit var mViewDataBinding: VDB

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    private var mProgressDialog: DialogHelper? = null

    abstract fun getViewModel(): VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        /*setLanguage(currentLanguage)*/
        performDataBinding()
    }

    private fun performDataBinding() {
        mViewModel = getViewModel()
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewDataBinding.executePendingBindings()
    }

    /**
     * To Show Progress
     * @param show: Boolean
     */
    open fun showProgress(show: Boolean) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.getProgressDialog(mActivity)
        }

        if (show) {
            mProgressDialog?.show(true)
        } else {
            mProgressDialog?.hide()
        }
    }

    open fun showError(error: String) {
        DialogFactory.showErrorDialog(mActivity, getString(R.string.dialog_title), error, null)
    }
}