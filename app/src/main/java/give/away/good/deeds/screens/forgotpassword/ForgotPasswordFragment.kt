package give.away.good.deeds.screens.forgotpassword

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.core.dialog.DialogFactory
import give.away.good.deeds.core.dialog.DialogListener
import give.away.good.deeds.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_forgot_password

    override fun getViewModel(): ForgotPasswordViewModel {
        return ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
    }

    override fun initViewModel() {
        mViewModel.isLoadingLiveData.observe(this) {
            showProgress(it)
        }

        mViewModel.forgotPassResponse.observe(this) { result ->
            if (result) {
                DialogFactory.showInfoDialog(mActivity, getString(R.string.action_forgot_password), getString(R.string.msg_reset_password_link_sent), listener = object : DialogListener {
                    override fun onClick(result: Boolean) {
                        findNavController().popBackStack()
                    }
                })
            } else {
                DialogFactory.showErrorDialog(mActivity, getString(R.string.action_forgot_password), getString(R.string.error_provided_email_address_not_register), null)
            }
        }
    }

    override fun onCreateView() {
        mViewModel.init()
        
        mViewDataBinding.btnSubmit.setOnClickListener {
            if (mViewModel.verifyForm(mViewDataBinding.inputEmailAdd)) {
                mViewModel.callForgotPass(mViewDataBinding.etEmailAdd.text.toString())
            }
        }

        mViewDataBinding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}