package give.away.good.deeds.ui.screens.main.post.add

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.PostRepository
import kotlinx.coroutines.launch

class AddPostViewModel(
    auth: FirebaseAuth,
    private val postRepository: PostRepository
) : ViewModel() {
    private val defaultLatLng = LatLng(51.509865, -0.118092)

    var post by mutableStateOf(Post(
        location = defaultLatLng,
        userId = auth.currentUser?.uid ?: ""
    ))
        private set

    fun onImagesChange(images: List<String>) {
        post = post.copy(images = images)
    }

    fun onTitleChange(title: String) {
        post = post.copy(title = title)
    }

    fun onDescriptionChange(description: String) {
        post = post.copy(description = description)
    }

    fun onQuantityChange(quantity: Int) {
        post = post.copy(quantity = quantity)
    }

    fun isValidPost() : Boolean {
        return post.title.isNotBlank() &&
                post.description.isNotBlank() &&
                post.images.isNotEmpty()
    }

    fun createPost() {
        viewModelScope.launch {
            val images = post.images.map { Uri.parse(it) }
            postRepository.createPost(post, images)
        }
    }


}