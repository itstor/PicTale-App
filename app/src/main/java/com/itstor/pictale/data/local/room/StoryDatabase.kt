package com.itstor.pictale.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itstor.pictale.data.local.entity.RemoteKeysEntity
import com.itstor.pictale.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}