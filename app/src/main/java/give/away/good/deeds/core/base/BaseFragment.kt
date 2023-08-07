package give.away.good.deeds.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import give.away.good.deeds.core.dialog.DialogFactory
import give.away.good.deeds.core.dialog.DialogHelper

abstract class BaseFragment<VBD : ViewDataBinding, VM : ViewModel>() : Fragment() {
    protected lateinit var mRootView: View
    protected lateinit var mViewDataBinding: VBD
    protected lateinit var mViewModel: VM
    protected lateinit var mActivity: AppCompatActivity
    private var mProgressDialog: DialogHelper? = null

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun getViewModel(): VM

    abstract fun onCreateView()

    abstract fun initViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as AppCompatActivity
        mViewModel = getViewModel()
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        mRootView = mViewDataBinding.root
        onCreateView()
        return mRootView
    }

    open fun showProgress(show: Boolean) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.getProgressDialog(mActivity)
        }

        if (show && isAdded) {
            mProgressDialog?.show(true)
        } else {
            mProgressDialog?.hide()
        }
    }

}