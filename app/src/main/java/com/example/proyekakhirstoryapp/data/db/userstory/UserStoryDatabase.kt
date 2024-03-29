package com.example.proyekakhirstoryapp.data.db.userstory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.db.remotekey.RemoteKeys
import com.example.proyekakhirstoryapp.data.db.remotekey.RemoteKeysDao

@Database(entities = [StoryModel::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class UserStoryDatabase : RoomDatabase() {

    abstract fun userStoryDao(): UserStoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: UserStoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserStoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserStoryDatabase::class.java, "user_story_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }

    }
}