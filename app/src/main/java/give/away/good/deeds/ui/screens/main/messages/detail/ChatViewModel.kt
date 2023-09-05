package give.away.good.deeds.ui.screens.main.messages.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import give.away.good.deeds.network.model.ChatGroupMessage
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.network.model.toChatMessage
import give.away.good.deeds.repository.COLLECTION_CHAT_GROUP
import give.away.good.deeds.repository.COLLECTION_CHAT_MESSAGES
import give.away.good.deeds.repository.CallResult
import give.away.good.deeds.repository.ChatRepository
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.NetworkReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

class ChatViewModel(
    private val networkReader: NetworkReader,
    private val chatRepository: ChatRepository,
    private val firestore: FirebaseFirestore,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<AppState<ChatGroupMessage>>(AppState.Loading)
    val uiState: StateFlow<AppState<ChatGroupMessage>> = _uiState.asStateFlow()

    fun fetchChatMessages(groupId: String) {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            val result = chatRepository.getChatMessages(groupId)
            if (result is CallResult.Success) {
                _uiState.emit(AppState.Result(result.data))
            } else {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
            }
        }
    }

    fun sendMessage(chatGroupMessage: ChatGroupMessage, value: String) {
        viewModelScope.launch {
            if(!networkReader.isConnected()){
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_INTERNET))
                return@launch
            }

            val chatMessage = ChatMessage(
                id = "",
                senderId = chatGroupMessage.me?.id ?: "",
                message = value
            )

            val groupId = chatGroupMessage.groupId
            val result = chatRepository.sendMessage(groupId, chatMessage)
            if (result is CallResult.Failure) {
                _uiState.emit(AppState.Error(cause = ErrorCause.NO_RESULT))
            }
        }
    }

    fun setSnapshotListener(chatGroupMessage: ChatGroupMessage) {
        firestore.collection(COLLECTION_CHAT_GROUP)
            .document(chatGroupMessage.groupId)
            .collection(COLLECTION_CHAT_MESSAGES)
            .addSnapshotListener { value, _ ->

                if(value != null) {
                    viewModelScope.launch {
                        val state = _uiState.value
                        if (state is AppState.Result) {
                            val chatMessages = (state.data?.messageList ?: emptyList()).toMutableSet()

                            val newList = value.documents.map {
                                it.toChatMessage()
                            }

                            newList.forEach { chat ->
                                if(!chatMessages.any { it.id == chat.id }){
                                    chatMessages.add(chat)
                                }
                            }

                            val finalList = chatMessages.toList().toMutableList()
                            finalList.sortBy {
                                it.timestamp
                            }
                            val newChatGroupMessage = chatGroupMessage.copy(
                                messageList = finalList
                            )
                            _uiState.emit(AppState.Result(newChatGroupMessage))
                        }
                    }
                }

            }
    }

}