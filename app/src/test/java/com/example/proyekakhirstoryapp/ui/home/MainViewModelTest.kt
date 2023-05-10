package com.example.proyekakhirstoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.proyekakhirstoryapp.data.DataDummy
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.utils.DiffUtilCallback
import com.example.proyekakhirstoryapp.utils.MainDispatcherRule
import com.example.proyekakhirstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private var dummyData = DataDummy.generateDummyStoriesEntity()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(userRepository)
        token =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUtMTjBnUm5PeHlZTFo1b18iLCJpYXQiOjE2ODM0MzM3MjB9.KL1oQqSXi-XY4orU8QUHsCZmHQ9TDnHAt0kjj3nuKfc"
    }

    @Test
    fun `when Success Get Stories Data and Not Null`() = runTest {
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = PagingData.from(dummyData)

        `when`(userRepository.getUserStoryList(token)).thenReturn(expectedStory)

        mainViewModel.getUserStories(token)

        val actualStory = mainViewModel.userStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = DiffUtilCallback(),
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Mockito.verify(userRepository).getUserStoryList(token)

        assertNotNull(differ.snapshot())
        assertEquals(dummyData.size, differ.snapshot().size)
        assertEquals(dummyData[0], differ.snapshot()[0])
    }

    @Test
    fun `when No Stories Data`() = runTest {
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = PagingData.from(emptyList())

        `when`(userRepository.getUserStoryList("random token")).thenReturn(expectedStory)

        mainViewModel.getUserStories("random token")

        val actualStory = mainViewModel.userStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = DiffUtilCallback(),
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Mockito.verify(userRepository).getUserStoryList("random token")
        assertEquals(0, differ.snapshot().size)
    }
}

val listUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}