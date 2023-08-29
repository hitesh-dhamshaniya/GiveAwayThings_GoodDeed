package give.away.good.deeds.ui.screens.authentication.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import give.away.good.deeds.R
import give.away.good.deeds.core.dialog.DialogFactory.showErrorDialog
import give.away.good.deeds.core.extension.getInputString
import give.away.good.deeds.core.extension.isEmailValid
import give.away.good.deeds.ui.screens.post_screens.MainActivity
import give.away.good.deeds.ui.screens.app_common.hideSoftKeyboard
import give.away.good.deeds.ui.screens.app_common.startActivity
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import org.koin.android.ext.android.inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by inject<LoginViewModel>()
    private lateinit var inputEmail: TextInputLayout
    private lateinit var edtEmailAdd: EditText
    private lateinit var inputPassword: TextInputLayout
    private lateinit var edtPassword: EditText
    private lateinit var progressIndicator: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressIndicator = view.findViewById(R.id.progressIndicator)
        inputEmail = view.findViewById(R.id.inputEmail)
        edtEmailAdd = view.findViewById(R.id.edtEmailAdd)
        inputPassword = view.findViewById(R.id.inputPassword)
        edtPassword = view.findViewById(R.id.edtPassword)

        handleUIClicks(view)

        observeUIChanges()
    }

    private fun handleUIClicks(view: View) {
        view.findViewById<View>(R.id.btnLogin).setOnClickListener {
            submitForm()
        }
        view.findViewById<View>(R.id.btnRegister).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        view.findViewById<View>(R.id.tvForgotPassword).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun observeUIChanges() {
        viewModel.uiState.observe(viewLifecycleOwner) { result ->
            progressIndicator.isVisible = false
            when (result) {
                is AuthenticationState.Loading -> {
                    progressIndicator.isVisible = true
                }

                is AuthenticationState.Result -> {
                    startActivity(MainActivity::class)
                }

                is AuthenticationState.Error -> {
                    showErrorDialog(requireActivity(), R.string.msg_login_failed, listener = null)
                }

                else -> {}
            }
        }
    }

    private fun submitForm() {
        if (verifyForm(inputEmail, inputPassword)) {
            hideSoftKeyboard()
            viewModel.login(edtEmailAdd.text.toString(), edtPassword.text.toString())
        }
    }

    /**
     * To verify the form field
     * @param email: String
     * @param password: String
     */
    private fun verifyForm(email: TextInputLayout, password: TextInputLayout): Boolean {
        email.error = null
        password.error = null
        var isValid = true
        if (email.editText?.getInputString()?.isEmpty() == true) {
            email.error = getString(R.string.error_please_enter_email_address)
            isValid = false
        }
        if (email.editText?.getInputString()?.isEmailValid() == false) {
            email.error = getString(R.string.error_please_enter_valid_email_address)
            isValid = false
        }
        if (password.editText?.getInputString()?.isEmpty() == true) {
            password.error = getString(R.string.error_please_enter_password)
            isValid = false
        }
        return isValid
    }
}