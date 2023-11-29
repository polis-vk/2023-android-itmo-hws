package ru.ok.itmo.tamtam.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.data.repository.MessageRepository
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.utils.Resource

class MessagePagingSource(
    private val messageRepository: MessageRepository,
    private val chatName: String,
    private val initialKey: Int,
    private val pageSize: Int,
    private val isLocal: Boolean
) : PagingSource<Int, Message>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> =
        withContext(Dispatchers.IO) {
            try {
                val key = params.key ?: initialKey
                val isAfter = params is LoadParams.Append
                val messagesResource = messageRepository.getMessages(
                    chatName = chatName,
                    lastKnownId = key,
                    count = pageSize,
                    isAfter = isAfter,
                    isLocal = isLocal
                )
                val messages = when (messagesResource) {
                    is Resource.Failure -> return@withContext LoadResult.Error(messagesResource.throwable)
                    is Resource.Success -> messagesResource.data
                }.sortedBy { it.id }


                val prevKey = if (isAfter) {
                    key - 1
                } else {
                    messages.firstOrNull()?.id?.let { it - 1 }
                }

                val nextKey = if (isAfter) {
                    messages.lastOrNull()?.id
                } else {
                    key
                }
                val last = messages.lastOrNull()?.id
                if (last != null) {
                    launch {
                        messageRepository.updateLastViewedForChat(
                            chatId = chatName,
                            lastViewedMessageId = last
                        )
                    }
                }


                return@withContext LoadResult.Page(
                    data = messages,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } catch (e: Exception) {
                return@withContext LoadResult.Error(e)
            }
        }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }
}