package give.away.good.deeds.ui.screens.main.post.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.PostRepository
import give.away.good.deeds.ui.screens.main.post.common.PostState
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository,
    private val networkReader: NetworkReader,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostState<List<Post>>>(PostState.Loading)
    val uiState: StateFlow<PostState<List<Post>>> = _uiState.asStateFlow()

    fun fetchPosts() {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(PostState.NoInternet)
                return@launch
            }

            _uiState.emit(PostState.Loading)
            val result = postRepository.getPost()
            if (result is CallResult.Success) {
                _uiState.emit(PostState.Result(result.data))
            } else {
                _uiState.emit(PostState.Empty)
            }
        }
    }

}