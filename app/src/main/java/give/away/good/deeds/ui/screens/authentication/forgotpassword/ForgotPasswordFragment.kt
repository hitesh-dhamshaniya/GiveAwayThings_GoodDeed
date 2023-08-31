package give.away.good.deeds.ui.screens.authentication.forgotpassword

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import give.away.good.deeds.R
import give.away.good.deeds.ui.screens.app_common.DialogFactory
import give.away.good.deeds.ui.screens.app_common.hideSoftKeyboard
import give.away.good.deeds.ui.screens.app_common.isValidEmail
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val viewModel by inject<ForgotPasswordViewModel>()
    private lateinit var inputEmailAdd: TextInputLayout
    private lateinit var etEmailAdd: EditText
    private lateinit var progressIndicator: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressIndicator = view.findViewById(R.id.progressIndicator)
        inputEmailAdd = view.findViewById(R.id.inputEmailAdd)
        etEmailAdd = view.findViewById(R.id.etEmailAdd)

        handleUIClicks(view)

        observeUIChanges()
    }

    private fun observeUIChanges() {
        viewModel.uiState.observe(viewLifecycleOwner) { result ->
            progressIndicator.isVisible = false
            when (result) {
                is AuthenticationState.Loading -> {
                    progressIndicator.isVisible = true
                }

                is AuthenticationState.Result -> {
                    showSuccessMessage()
                }

                is AuthenticationState.Error -> {
                    showErrorMessage()
                }

                else -> {}
            }
        }
    }

    private fun handleUIClicks(view: View) {
        view.findViewById<View>(R.id.btnSubmit).setOnClickListener {
            submitForm()
        }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun submitForm() {
        if (verifyForm(inputEmailAdd)) {
            hideSoftKeyboard()
            viewModel.forgotPassword(etEmailAdd.text.toString())
        }
    }

    /**
     * TO verifyForm
     * @param emailAddress: TextInputLayout
     * @return result: Boolean
     */
    private fun verifyForm(emailAddress: TextInputLayout): Boolean {
        var validForm = false
        if (emailAddress.editText?.text?.toString()?.isEmpty() == true) {
            emailAddress.error = getString(R.string.error_please_enter_email_address)
        } else if (emailAddress.editText?.isValidEmail() != true) {
            emailAddress.error = getString(R.string.error_please_enter_valid_email_address)
        } else {
            emailAddress.error = null
            validForm = true
        }
        return validForm
    }

    private fun showErrorMessage() {
        DialogFactory.showErrorDialog(
            requireActivity(),
            getString(R.string.action_forgot_password),
            getString(R.string.error_provided_email_address_not_register)
        )
    }

    private fun showSuccessMessage() {
        DialogFactory.showInfoDialog(
            requireActivity(),
            getString(R.string.action_forgot_password),
            getString(R.string.msg_reset_password_link_sent),
            listener = {
                findNavController().popBackStack()
            }
        )
    }

}