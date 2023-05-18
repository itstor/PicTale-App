package com.itstor.pictale.ui.views.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.google.gson.Gson
import com.itstor.pictale.common.Result
import com.itstor.pictale.data.AuthRepository
import com.itstor.pictale.data.StoryRepository
import com.itstor.pictale.data.remote.response.GeneralResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val storyRepo: StoryRepository,
    private val authRepo: AuthRepository
) : ViewModel() {
    val storiesParams = MutableLiveData<Triple<Int?, Int?, Int?>>()
    val stories = storiesParams.switchMap { (page, limit, location) ->
        getAllStories(page, limit, location)
    }

    fun getAllStories(page: Int? = null, limit: Int? = null, location: Int? = null) = liveData {
        emit(Result.loading(null))
        try {
            val token = authRepo.getAuthToken().first() ?: ""

            val response = storyRepo.getAllStories(token, page, limit, location)

            emit(Result.success(response.listStories))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(errorResponse, GeneralResponse::class.java)
                emit(Result.error(null, errorBody.message))

                return@liveData
            }
            Log.d(TAG, "getAllStories error: ${e.message}")
            emit(Result.error(null, null))
        }
    }

    companion object {
        private const val TAG = "FeedViewModel"
    }
}