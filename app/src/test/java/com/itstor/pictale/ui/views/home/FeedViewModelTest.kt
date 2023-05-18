package com.itstor.pictale.ui.views.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.itstor.pictale.DataDummy
import com.itstor.pictale.MainDispatcherRule
import com.itstor.pictale.data.AuthRepository
import com.itstor.pictale.data.StoryRepository
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.ui.adapter.StoryListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepo: StoryRepository

    @Mock
    private lateinit var authRepo: AuthRepository

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val authToken = "dummy_token"
        `when`(authRepo.getAuthToken()).thenReturn(flowOf(authToken))

        val dummyStory = DataDummy.generateDummyStoryEntity()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        `when`(storyRepo.getAllStoriesWithPaging(authToken)).thenReturn(expectedStory.asFlow())

        val mainViewModel = FeedViewModel(storyRepo, authRepo)
        val actualStory: PagingData<StoryEntity> = mainViewModel.getStories().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        // Memastikan data tidak null
        Assert.assertNotNull(differ.snapshot())
        // Memastikan jumlah data sesuai dengan dummy story
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        // Memastikan data pertama sesuai dengan dummy story
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val authToken = "dummy_token"
        `when`(authRepo.getAuthToken()).thenReturn(flowOf(authToken))

        val data: PagingData<StoryEntity> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        `when`(storyRepo.getAllStoriesWithPaging(authToken)).thenReturn(expectedStory.asFlow())

        val mainViewModel = FeedViewModel(storyRepo, authRepo)
        val actualStory: PagingData<StoryEntity> = mainViewModel.getStories().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        // Memastikan jumlah data yang dikembalikan nol.
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}