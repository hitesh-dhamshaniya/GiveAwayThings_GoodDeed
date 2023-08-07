package give.away.good.deeds.screens.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseViewModel
import give.away.good.deeds.core.extension.isEmailValid
import give.away.good.deeds.network.ApiConstants
import give.away.good.deeds.network.model.User


class RegisterViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var auth: FirebaseAuth
    val registerResponse: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        const val TAG: String = "RegisterViewModel"
    }

    fun init() {
        auth = Firebase.auth
    }


    /**
     * to validate form
     */
    fun validateForm(fName: TextInputLayout, lName: TextInputLayout, email: TextInputLayout, password: TextInputLayout, confirmPassword: TextInputLayout): Boolean {
        var result = false

        fName.error = null
        lName.error = null
        email.error = null
        password.error = null
        confirmPassword.error = null

        if (fName.editText?.text?.toString()?.isEmpty() == true) {
            fName.error = mContext.getString(R.string.error_please_enter_first_name)
        } else if (lName.editText?.text?.toString()?.isEmpty() == true) {
            lName.error = mContext.getString(R.string.error_please_enter_last_name)
        } else if (email.editText?.text?.toString()?.isEmpty() == true) {
            email.error = mContext.getString(R.string.error_please_enter_email_address)
        } else if (!email.editText?.text.toString().isEmailValid()) {
            email.error = mContext.getString(R.string.error_please_enter_valid_email_address)
        } else if (password.editText?.text.toString().isEmpty()) {
            password.error = mContext.getString(R.string.error_please_enter_password)
        } else if (confirmPassword.editText?.text?.toString()?.isEmpty() == true) {
            confirmPassword.error = mContext.getString(R.string.error_please_enter_confirm_password)
        } else if (password.editText?.text.toString() != confirmPassword.editText?.text.toString()) {
            confirmPassword.error = mContext.getString(R.string.error_password_and_confirm_password_not_matched)
        } else {
            result = true
        }
        return result
    }

    /**
     * @param email: String
     * @param password: String
     */
    fun createUserWithEmailAndPassword(firstName: String, lastName: String, email: String, password: String) {
        isLoadingLiveData.postValue(true)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(TAG, "createUserWithEmailAndPassword: Successful ${auth.uid}")
                if (auth.uid != null) {
                    saveUser(auth.uid ?: "", firstName = firstName, lastName, email)
                } else {
                    isLoadingLiveData.postValue(false)
                    registerResponse.postValue(false)
                }
            } else {
                isLoadingLiveData.postValue(false)
                Log.e(TAG, "createUserWithEmailAndPassword: Failed")
            }
        }
    }


    /**
     * Save user details to Firestore
     * @param userId: String
     * @param firstName: String
     * @param lastName: String
     * @param email: String
     */
    private fun saveUser(userId: String, firstName: String, lastName: String, email: String) {
        val db = FirebaseFirestore.getInstance()
        val user = User(userId, firstName, lastName, email)
        db.collection(ApiConstants.USERS).document(userId)
            .set(user)
            .addOnSuccessListener(OnSuccessListener<Void?> {
                // User data saved successfully
                registerResponse.postValue(true)
                isLoadingLiveData.postValue(false)
            })
            .addOnFailureListener(OnFailureListener {
                // Failed to save user data
                // Handle the error
                Log.e("Register", "Failure ==> ${it.printStackTrace()}")
                registerResponse.postValue(false)
                isLoadingLiveData.postValue(false)
            })
    }


}