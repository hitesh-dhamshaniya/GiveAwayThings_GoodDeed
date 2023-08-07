package give.away.good.deeds.screens.register

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.core.dialog.DialogFactory
import give.away.good.deeds.core.dialog.DialogListener
import give.away.good.deeds.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_register

    override fun getViewModel(): RegisterViewModel {
        return ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun initViewModel() {
        mViewModel.isLoadingLiveData.observe(this) {
            showProgress(it)
        }

        mViewModel.registerResponse.observe(this) {
            if (it) {
                DialogFactory.showInfoDialog(mActivity, getString(R.string.action_register), "Registration successfully done",
                    listener = object : DialogListener {
                        override fun onClick(result: Boolean) {
                            findNavController().popBackStack()
                        }
                    })
            }
        }
    }

    override fun onCreateView() {
        mViewModel.init()
        mViewDataBinding.isShowingForm = false
        mViewDataBinding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        mViewDataBinding.btnRegister.setOnClickListener {
            mViewDataBinding.isShowingForm = true
        }

        mViewDataBinding.btnSubmit.setOnClickListener {

            val isValid = mViewModel.validateForm(
                mViewDataBinding.inputFirstName,
                mViewDataBinding.inputLastName,
                mViewDataBinding.inputEmail,
                mViewDataBinding.inputPassword,
                mViewDataBinding.inputConfirmPassword,
            )

            if (isValid) {
                mViewModel.createUserWithEmailAndPassword(
                    mViewDataBinding.edtFirstName.text.toString().trim(),
                    mViewDataBinding.edtLastName.text.toString().trim(),
                    mViewDataBinding.edtEmailAdd.text.toString().trim(),
                    mViewDataBinding.edtPassword.text.toString().trim(),
                )
            }
        }
    }
}