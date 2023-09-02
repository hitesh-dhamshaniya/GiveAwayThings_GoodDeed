package give.away.good.deeds.ui.screens.main.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.PostRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<Post>>(AppState.Loading)
    val uiState: StateFlow<AppState<Post>> = _uiState.asStateFlow()

    fun isMyPost(post: Post): Boolean{
        return authRepository.getUserId() == post.userId
    }
    fun getPost(postId: String) {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            when (val result = postRepository.getPost(postId)) {
                is CallResult.Success -> {
                    _uiState.emit(AppState.Result(result.data))
                }
                else -> {
                    _uiState.emit(AppState.Error())
                }
            }
        }
    }
}