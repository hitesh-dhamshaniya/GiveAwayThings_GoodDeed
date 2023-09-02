package give.away.good.deeds.ui.screens.main.messages.detail

import androidx.lifecycle.ViewModel
import give.away.good.deeds.repository.ChatRepository
import give.away.good.deeds.utils.NetworkReader

class ChatViewModel(
    private val networkReader: NetworkReader,
    private val chatRepository: ChatRepository
) : ViewModel() {
}