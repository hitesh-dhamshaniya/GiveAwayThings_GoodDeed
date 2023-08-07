package give.away.good.deeds.screens.forgotpassword

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseViewModel
import give.away.good.deeds.core.extension.isEmailValid

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var auth: FirebaseAuth

    fun init() {
        auth = Firebase.auth
    }

    /**
     * TO verifyForm
     * @param emailAddress: TextInputLayout
     * @return result: Boolean
     */
    fun verifyForm(emailAddress: TextInputLayout): Boolean {
        var validForm = false
        emailAddress.error = null
        if (emailAddress.editText?.text?.toString()?.isEmpty() == true) {
            emailAddress.error = mContext.getString(R.string.error_please_enter_email_address)
        } else if (!emailAddress.editText?.text.toString().isEmailValid()) {
            emailAddress.error = mContext.getString(R.string.error_please_enter_valid_email_address)
        } else {
            validForm = true
        }
        return validForm
    }

    val forgotPassResponse: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Call forgot password API, send reset password link to email
     * @param email: String
     */
    fun callForgotPass(email: String) {
        isLoadingLiveData.postValue(true)
        auth.sendPasswordResetEmail(email).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                forgotPassResponse.postValue(true)
            } else {
                forgotPassResponse.postValue(false)
            }
            isLoadingLiveData.postValue(false)
        }
    }
}