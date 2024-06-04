package com.example.rabobankassignment.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.rabobankassignment.MainCoroutineRule
import com.example.rabobankassignment.data.FakeRepository
import com.example.rabobankassignment.data.Repository
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.getOrAwaitValue
import com.example.rabobankassignment.observeForTesting
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // Subject under test
    private lateinit var mainViewModel: MainViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        repository = FakeRepository()
        val issue1 = Issue("Name1", "Surname1",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        val issue2 = Issue("Name2", "Surname2",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        val issue3 = Issue("Name3", "Surname3",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        repository.addIssues(issue1, issue2, issue3)

        mainViewModel = MainViewModel(repository)
    }
    @Test
    fun clearCompletedTasks_clearsTasks() = runTest {
        assertThat(mainViewModel.items.value).isNotEmpty()
//        mainViewModel.items.observeForTesting {
//            assertThat(mainViewModel.items.getOrAwaitValue()).isNotEmpty()
//        }
    }
}