package give.away.good.deeds.screens.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseViewModel
import give.away.good.deeds.core.extension.getInputString
import give.away.good.deeds.core.extension.isEmailValid
import give.away.good.deeds.network.ApiConstants
import give.away.good.deeds.network.model.User
import give.away.good.deeds.sharePref.service.ProfilePrefService
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var auth: FirebaseAuth
    val loginResponseLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val profilePrefService: ProfilePrefService by inject(ProfilePrefService::class.java)

    fun init() {
        auth = Firebase.auth
    }

    /**
     * To verify the form field
     * @param email: String
     * @param password: String
     */
    fun verifyForm(email: TextInputLayout, password: TextInputLayout): Boolean {
        email.error = null
        password.error = null
        var isValid = false
        if (email.editText?.getInputString()?.isEmpty() == true) {
            email.error = mContext.getString(R.string.error_please_enter_email_address)
        } else if (email.editText?.getInputString()?.isEmailValid() == false) {
            email.error = mContext.getString(R.string.error_please_enter_valid_email_address)
        } else if (password.editText?.getInputString()?.isEmpty() == true) {
            password.error = mContext.getString(R.string.error_please_enter_password)
        } else {
            isValid = true
        }
        return isValid
    }

    /**
     * TO login API call
     * @param email: String
     * @param password: String
     */
    fun callLogin(email: String, password: String) {
        isLoadingLiveData.postValue(true)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                viewModelScope.launch {
                    val user: User? = getUserDetails()
                    if (user != null) {
                        profilePrefService.saveUser(user)
                        loginResponseLiveData.postValue(true)
                    }
                    isLoadingLiveData.postValue(false)
                }
            } else {
                loginResponseLiveData.postValue(false)
                isLoadingLiveData.postValue(false)
            }
        }
    }

    fun getUserDetails(userId: String) {
        val db = Firebase.firestore.collection(ApiConstants.USERS).document(userId)
    }

    private suspend fun getUserDetails(): User? {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userDocument = Firebase.firestore.collection(ApiConstants.USERS).document(currentUser.uid).get().await()
            return userDocument.toObject(User::class.java)
        }
        return null
    }
}