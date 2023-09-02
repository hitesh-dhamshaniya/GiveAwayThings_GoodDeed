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
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.PostRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddPostViewModel(
    auth: FirebaseAuth,
    private val postRepository: PostRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val defaultLatLng = LatLng(51.509865, -0.118092)
    private val defaultPost = Post(
        location = defaultLatLng,
        userId = auth.currentUser?.uid ?: ""
    )

    private val _uiState = MutableStateFlow<AppState<Unit>>(AppState.Ideal)
    val uiState: StateFlow<AppState<Unit>> = _uiState.asStateFlow()

    var post by mutableStateOf(defaultPost)
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
            if (!networkReader.isConnected()) {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            val images = post.images.map { Uri.parse(it) }
            when (val result = postRepository.createPost(post, images)) {
                is CallResult.Success -> {
                    _uiState.emit(AppState.Result())
                }

                is CallResult.Failure -> {
                    _uiState.emit(AppState.Error(result.message ?: ""))
                }
            }
        }
    }

    fun resetState() {
        resetNetworkState()
        post = defaultPost
    }

    fun resetNetworkState() {
        viewModelScope.launch {
            if (!networkReader.isConnected()) {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            } else {
                _uiState.emit(AppState.Ideal)
            }
        }
    }


}