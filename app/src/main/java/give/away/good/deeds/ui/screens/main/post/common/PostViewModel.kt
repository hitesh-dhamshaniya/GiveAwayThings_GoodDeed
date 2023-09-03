package give.away.good.deeds.ui.screens.main.post.common

import androidx.lifecycle.ViewModel
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.AuthRepository

class PostViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isMyPost(post: Post): Boolean{
        return authRepository.getUserId() == post.userId
    }

}