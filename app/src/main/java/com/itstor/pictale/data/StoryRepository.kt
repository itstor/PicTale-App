package com.itstor.pictale.data

import com.itstor.pictale.data.remote.retrofit.ApiService
import com.itstor.pictale.utils.ApiUtils
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
) {
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