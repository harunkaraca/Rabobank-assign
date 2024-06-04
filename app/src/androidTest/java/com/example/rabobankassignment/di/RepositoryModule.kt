package com.example.rabobankassignment.di

import com.example.rabobankassignment.data.BaseRepository
import com.example.rabobankassignment.data.FakeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * TasksRepository binding to use in tests.
 *
 * Hilt will inject a [FakeRepository] instead of a [DefaultTasksRepository].
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class RepositoryModuler {
    @Singleton
    @Binds
    abstract fun bindRepository(repo: FakeRepository): BaseRepository
}
