package com.itstor.pictale.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itstor.pictale.data.AuthRepository
import com.itstor.pictale.data.StoryRepository
import com.itstor.pictale.data.local.entity.StoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val storyRepo: StoryRepository,
    private val authRepo: AuthRepository
) : ViewModel() {
    fun getStories(): Flow<PagingData<StoryEntity>> {
        return flow {
            val authToken = authRepo.getAuthToken().firstOrNull() ?: ""
            val stories = storyRepo.getAllStoriesWithPaging(authToken).cachedIn(viewModelScope)
            emitAll(stories)
        }
    }

    companion object {
        private const val TAG = "FeedViewModel"
    }
}