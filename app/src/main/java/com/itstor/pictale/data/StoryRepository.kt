package com.itstor.pictale.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.itstor.pictale.data.local.StoryRemoteMediator
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.data.local.room.StoryDatabase
import com.itstor.pictale.data.remote.retrofit.ApiService
import com.itstor.pictale.utils.ApiUtils
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase
) {
    fun getAllStoriesWithPaging(
        token: String,
        location: Int? = null
    ): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 30
            ),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                ApiUtils.formatToken(token),
                location
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).flow
    }

    suspend fun getAllStories(
        token: String,
        page: Int? = null,
        limit: Int? = null,
        location: Int? = null
    ) = apiService.getAllStories(ApiUtils.formatToken(token), page, limit, location)

    suspend fun getDetailStory(token: String, storyId: String) = apiService.getDetailStory(
        ApiUtils.formatToken(token),
        storyId
    )

    suspend fun addStory(
        token: String,
        description: String,
        photo: MultipartBody.Part,
        lat: Float? = null,
        lon: Float? = null
    ) = apiService.addStory(ApiUtils.formatToken(token), description, photo, lat, lon)
}