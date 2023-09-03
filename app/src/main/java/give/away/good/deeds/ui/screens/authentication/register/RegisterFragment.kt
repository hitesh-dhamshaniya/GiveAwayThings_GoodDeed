package give.away.good.deeds.ui.screens.authentication.register

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
import give.away.good.deeds.ui.screens.app_common.startActivity
import give.away.good.deeds.ui.screens.authentication.common.AuthenticationState
import give.away.good.deeds.ui.screens.main.MainActivity
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel by inject<RegisterViewModel>()

    private lateinit var progressIndicator: View
    private lateinit var inputFirstName: TextInputLayout
    private lateinit var inputLastName: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputConfirmPassword: TextInputLayout

    private lateinit var edtFirstName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmailAdd: EditText
    private lateinit var edtPassword: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressIndicator = view.findViewById(R.id.progressIndicator)
        inputFirstName = view.findViewById(R.id.inputFirstName)
        inputLastName = view.findViewById(R.id.inputLastName)
        inputEmail = view.findViewById(R.id.inputEmail)
        inputPassword = view.findViewById(R.id.inputPassword)
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword)

        edtFirstName = view.findViewById(R.id.edtFirstName)
        edtLastName = view.findViewById(R.id.edtLastName)
        edtEmailAdd = view.findViewById(R.id.edtEmailAdd)
        edtPassword = view.findViewById(R.id.edtPassword)

        handleUIClicks(view)

        observeUIChanges()
    }

    private fun handleUIClicks(view: View) {
        view.findViewById<View>(R.id.btnSubmit).setOnClickListener {
            val isValid = validateForm(
                inputFirstName,
                inputLastName,
                inputEmail,
                inputPassword,
                inputConfirmPassword,
            )

            if (isValid) {
                hideSoftKeyboard()
                viewModel.register(
                    edtFirstName.text.toString().trim(),
                    edtLastName.text.toString().trim(),
                    edtEmailAdd.text.toString().trim(),
                    edtPassword.text.toString().trim(),
                )
            }
        }
        view.findViewById<View>(R.id.btnRegister).setOnClickListener {
            view.findViewById<View>(R.id.joiningNoteGroup).isVisible = false
            view.findViewById<View>(R.id.formGroup).isVisible = true
        }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            findNavController().popBackStack()
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
                    showSuccessMessage()
                }

                is AuthenticationState.Error -> {
                    showErrorMessage(result.message)
                }

                is AuthenticationState.NoInternet -> {
                    DialogFactory.showErrorDialog(
                        requireActivity(),
                        getString(R.string.action_register),
                        getString(R.string.error_no_internet)
                    )
                }

                else -> {}
            }
        }
    }

    /**
     * to validate form
     */
    private fun validateForm(
        fName: TextInputLayout,
        lName: TextInputLayout,
        email: TextInputLayout,
        password: TextInputLayout,
        confirmPassword: TextInputLayout
    ): Boolean {

        fName.error = null
        lName.error = null
        email.error = null
        password.error = null
        confirmPassword.error = null

        var isValid = true

        if (fName.editText?.text?.toString()?.isEmpty() == true) {
            isValid = false
            fName.error = getString(R.string.error_please_enter_first_name)
        }
        if (lName.editText?.text?.toString()?.isEmpty() == true) {
            isValid = false
            lName.error = getString(R.string.error_please_enter_last_name)
        }
        if (email.editText?.text?.toString()?.isEmpty() == true) {
            isValid = false
            email.error = getString(R.string.error_please_enter_email_address)
        }
        if (email.editText?.isValidEmail() != true) {
            isValid = false
            email.error = getString(R.string.error_please_enter_valid_email_address)
        }
        if (password.editText?.text.toString().isEmpty()) {
            isValid = false
            password.error = getString(R.string.error_please_enter_password)
        }
        if (password.editText?.text.toString().length < 6) {
            isValid = false
            password.error = getString(R.string.error_please_enter_valid_password)
        }
        if (confirmPassword.editText?.text?.toString()?.isEmpty() == true) {
            isValid = false
            confirmPassword.error = getString(R.string.error_please_enter_confirm_password)
        }
        if (password.editText?.text.toString() != confirmPassword.editText?.text.toString()) {
            isValid = false
            confirmPassword.error =
                getString(R.string.error_password_and_confirm_password_not_matched)
        }
        return isValid
    }

    private fun showSuccessMessage() {
        DialogFactory.showInfoDialog(
            requireActivity(),
            getString(R.string.action_register),
            "Registration successfully done",
            listener = {
                startActivity(MainActivity::class)
                requireActivity().finish()
            }
        )
    }

    private fun showErrorMessage(message: String) {
        DialogFactory.showErrorDialog(
            requireActivity(),
            getString(R.string.action_register),
            message
        )
    }
}