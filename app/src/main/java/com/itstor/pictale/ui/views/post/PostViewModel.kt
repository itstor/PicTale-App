package com.itstor.pictale.ui.views.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.itstor.pictale.common.Result
import com.itstor.pictale.data.AuthRepository
import com.itstor.pictale.data.StoryRepository
import com.itstor.pictale.data.remote.response.GeneralResponse
import com.itstor.pictale.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val storyRepo: StoryRepository,
    private val authRepo: AuthRepository
) : ViewModel() {
    fun uploadStory(imageFile: File, caption: String) = liveData {
        emit(Result.loading(null))
        try {
            val compressedImage = FileUtils.reduceFileImage(imageFile)
            val token = authRepo.getAuthToken().first() ?: ""
            val imageMultiPart = MultipartBody.Part.createFormData(
                "photo",
                compressedImage.name,
                compressedImage.asRequestBody()
            )
            val result = storyRepo.addStory(token, caption, imageMultiPart)
            emit(Result.success(result))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(errorResponse, GeneralResponse::class.java)
                emit(Result.error(null, errorBody.message))
                return@liveData
            }
            emit(Result.error(null, e.message))
        }
    }
}