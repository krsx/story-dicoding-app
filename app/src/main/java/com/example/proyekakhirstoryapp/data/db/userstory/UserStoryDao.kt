package com.example.proyekakhirstoryapp.data.db.userstory

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyekakhirstoryapp.data.db.model.StoryModel

@Dao
interface UserStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStory(story: List<StoryModel>)

    @Query("SELECT * FROM user_story")
    fun getAllUserStories(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM user_story")
    suspend fun deleteAllUserStories()

}