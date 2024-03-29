package com.example.proyekakhirstoryapp.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiService
import com.example.proyekakhirstoryapp.data.db.userstory.UserStoryDatabase
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.db.remotekey.RemoteKeys
import retrofit2.awaitResponse
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val userStoryDatabase: UserStoryDatabase,
    private val apiService: ApiService,
    private val token: String,
) : RemoteMediator<Int, StoryModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryModel>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val formatToken = "Bearer $token"
            val response = apiService.getAllStories(
                token = formatToken,
                location = 1,
                page = page,
                size = state.config.pageSize,
            ).awaitResponse().body()

            val endOfPaginationReached = response?.listStory?.isEmpty()

            Log.e("StoryRemoteMediator", "inserting: $response")

            userStoryDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    userStoryDatabase.remoteKeysDao().deleteAllRemoteKeys()
                    userStoryDatabase.userStoryDao().deleteAllUserStories()
                }

                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                val keys = response.listStory.map {
                    RemoteKeys(
                        id = it?.id!!,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                userStoryDatabase.remoteKeysDao().insertAll(keys)

                val responseData = response.listStory.map {
                    StoryModel(
                        it?.id!!,
                        it.name,
                        it.description,
                        it.photoUrl,
                        it.createdAt,
                        it.lat as Double?,
                        it.lon as Double?,
                    )
                }

                userStoryDatabase.userStoryDao().insertUserStory(responseData)


            }
            MediatorResult.Success(endOfPaginationReached!!)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryModel>) : RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            userStoryDatabase.remoteKeysDao().getRemoteKey(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryModel>) : RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            userStoryDatabase.remoteKeysDao().getRemoteKey(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, StoryModel>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                userStoryDatabase.remoteKeysDao().getRemoteKey(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}