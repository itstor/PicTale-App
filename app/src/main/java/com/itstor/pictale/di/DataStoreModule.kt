package com.itstor.pictale.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.itstor.pictale.data.local.AuthPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "prefs")

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    @Singleton
    fun provideAuthPreferences(dataStore: DataStore<Preferences>) = AuthPreferences(dataStore)
}