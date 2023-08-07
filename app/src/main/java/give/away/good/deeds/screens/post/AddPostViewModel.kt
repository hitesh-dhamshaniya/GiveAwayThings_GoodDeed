package give.away.good.deeds.screens.post

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import give.away.good.deeds.core.base.BaseViewModel
import give.away.good.deeds.network.model.GiveAwayModel

class AddPostViewModel(application: Application) : BaseViewModel(application = application) {
    private var db = Firebase.firestore
    private val giveAwayItemPost = "GiveAwayItemPost"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    /**
     * To create post
     * @param giveAwayPost
     */
    fun createPost(giveAwayPost: GiveAwayModel) {
        val docRef = db.collection(giveAwayItemPost)
        docRef.add(giveAwayPost.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }
}