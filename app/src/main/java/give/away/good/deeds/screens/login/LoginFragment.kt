package give.away.good.deeds.screens.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.core.dialog.DialogFactory.showErrorDialog
import give.away.good.deeds.databinding.FragmentLoginBinding
import give.away.good.deeds.home.MainActivity

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_login

    override fun getViewModel(): LoginViewModel {
        return ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView() {
        mViewModel.init()
        initUI(mViewDataBinding)
    }

    override fun initViewModel() {
        mViewModel.isLoadingLiveData.observe(this) { loading ->
            showProgress(loading)
        }

        mViewModel.loginResponseLiveData.observe(this) { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            } else {
                showErrorDialog(mActivity, R.string.msg_login_failed, listener = null)
            }
        }
    }

    private fun initUI(binding: FragmentLoginBinding) {
        binding.btnLogin.setOnClickListener {
            if (mViewModel.verifyForm(binding.inputEmail, binding.inputPassword)) {
                mViewModel.callLogin(binding.edtEmailAdd.text.toString(), binding.edtPassword.text.toString())
            }
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}