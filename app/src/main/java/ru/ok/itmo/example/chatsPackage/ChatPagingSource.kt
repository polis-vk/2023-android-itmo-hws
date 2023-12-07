package ru.ok.itmo.example.chatsPackage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.ok.itmo.example.retrofit.RetrofitProvider


class ChatPagingSource(private val chatUseCase: ChatUseCase) : PagingSource<Int, Channel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Channel> {
        return try {
            val pageNumber = params.key ?: 1
            val response = chatUseCase.getChannelsByPage(pageNumber, params.loadSize)

            LoadResult.Page(
                data = response,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (response.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Channel>): Int? {
        return state.anchorPosition
    }
}

