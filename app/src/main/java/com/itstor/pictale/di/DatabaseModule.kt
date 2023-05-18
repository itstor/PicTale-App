package com.itstor.pictale.di

import android.content.Context
import androidx.room.Room
import com.itstor.pictale.data.local.room.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideStoryDatabase(@ApplicationContext appContext: Context): StoryDatabase {
        return Room.databaseBuilder(
            appContext,
            StoryDatabase::class.java, "story_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}