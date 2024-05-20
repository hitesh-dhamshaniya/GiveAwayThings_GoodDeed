package give.away.good.deeds.ui.screens.main.post.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    fun isMyPost(post: Post): Boolean {
        return authRepository.getUserId() == post.userId
    }

    fun sendPush(postId: String) {
        viewModelScope.launch {
            postRepository.broadcastNewPost(postId)
        }
    }

}