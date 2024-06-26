package com.example.rabobankassignment.di

import android.app.DownloadManager
import android.content.Context
import com.example.rabobankassignment.Downloader.FileDownloader
import com.example.rabobankassignment.data.BaseRepository
import com.example.rabobankassignment.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideFileDownloader(@ApplicationContext context: Context,ioDispatcher: CoroutineDispatcher):FileDownloader{
        return FileDownloader(context,ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(fileDownloader: FileDownloader,ioDispatcher: CoroutineDispatcher):BaseRepository{
        return Repository(fileDownloader,ioDispatcher)
    }
}