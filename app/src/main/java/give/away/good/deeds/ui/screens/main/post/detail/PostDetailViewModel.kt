package give.away.good.deeds.ui.screens.main.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.network.model.PostInfo
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.ChatRepository
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
    private val chatRepository: ChatRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<PostInfo>>(AppState.Loading)
    val uiState: StateFlow<AppState<PostInfo>> = _uiState.asStateFlow()

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

    fun setPostStatus(post: Post, status: Int) {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            when (val result = postRepository.updatePostStatus(post.id, status)) {
                is CallResult.Success -> {
                    _uiState.emit(AppState.Result())
                }

                is CallResult.Failure -> {
                    _uiState.emit(AppState.Error(message = result.message ?: ""))
                }
            }
        }
    }

    fun sendRequest(post: Post) {
        // hitesh@yopmail.com
        viewModelScope.launch {
            _uiState.emit(AppState.Loading)

            when (val result = postRepository.requestPost(post.id)) {
                is CallResult.Success -> {
                    createChatGroup( post)
                }

                is CallResult.Failure -> {
                    _uiState.emit(AppState.Error(message = result.message ?: ""))
                }
            }
        }
    }

    private suspend fun createChatGroup(post: Post){
        when (val result = chatRepository.createChatGroup(post.userId)) {
            is CallResult.Success -> {
                sendMessage(result.data, post)
            }

            is CallResult.Failure -> {
                _uiState.emit(AppState.Error(message = result.message ?: ""))
            }
        }
    }

    private suspend fun sendMessage(groupId: String, post: Post) {
        val chatMessage = ChatMessage(
            id = "",
            senderId = authRepository.getUserId() ?: "",
            message = "I'm interested in \"${post.title}\". Can I please have it? "
        )
        when (val result = chatRepository.sendMessage(groupId, chatMessage)) {
            is CallResult.Success -> {
                _uiState.emit(AppState.Result())
            }

            is CallResult.Failure -> {
                _uiState.emit(AppState.Error(message = result.message ?: ""))
            }
        }
    }
}