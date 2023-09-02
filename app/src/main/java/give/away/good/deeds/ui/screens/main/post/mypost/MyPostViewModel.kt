package give.away.good.deeds.ui.screens.main.post.mypost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MyPostViewModel(
    private val postRepository: PostRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<List<Post>>>(AppState.Loading)
    val uiState: StateFlow<AppState<List<Post>>> = _uiState.asStateFlow()

    fun fetchPosts() {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            _uiState.emit(AppState.Loading)
            val result = postRepository.getMyPosts()
            if (result is CallResult.Success) {
                _uiState.emit(AppState.Result(result.data))
            } else {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
            }
        }
    }

}