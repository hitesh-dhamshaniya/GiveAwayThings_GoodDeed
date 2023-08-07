package give.away.good.deeds.screens.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import give.away.good.deeds.core.base.BaseViewModel
import give.away.good.deeds.network.ApiConstants
import give.away.good.deeds.network.model.User
import give.away.good.deeds.sharePref.service.ProfilePrefService
import org.koin.java.KoinJavaComponent.inject

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val profilePrefService: ProfilePrefService by inject(ProfilePrefService::class.java)

    val profilePicUploadLiveData: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * @param uri: Uri
     */
    fun uploadImage(uri: Uri) {
        isLoadingLiveData.postValue(true)
        val userDocRef = Firebase.firestore.collection(ApiConstants.USERS).document(profilePrefService.getUserId())
        val userName: String = profilePrefService.getFirstName()
        val imageRef = storageRef.child("${ApiConstants.USER_PROFILE_PICS}/${userName}_${System.currentTimeMillis()}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener { downloadUri->

                // Image uploaded successfully
                profilePicUploadLiveData.postValue(true)
                isLoadingLiveData.postValue(false)
            }
            .addOnFailureListener {
                // Handle upload failure
                profilePicUploadLiveData.postValue(false)
                isLoadingLiveData.postValue(false)
            }
    }
}