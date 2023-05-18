package com.itstor.pictale.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.data.remote.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
    private val authToken: String,
    private val location: Int?
) : PagingSource<Int, StoryEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getAllStories(authToken, page, params.loadSize, location)

            val data = responseData.listStories?.map {
                StoryEntity(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    photoUrl = it.photoUrl,
                    lat = it.lat,
                    lon = it.lon,
                    createdAt = it.createdAt,
                )
            } ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
