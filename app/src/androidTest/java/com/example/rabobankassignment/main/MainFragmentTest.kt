package com.example.rabobankassignment.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.rabobankassignment.MainActivity
import com.example.rabobankassignment.R
import com.example.rabobankassignment.data.BaseRepository
import com.example.rabobankassignment.data.FakeRepository
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.Result
import com.example.rabobankassignment.launchFragmentInHiltContainer
import com.example.rabobankassignment.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainFragmentTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: FakeRepository

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun noDownloadedFile(){
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        onView(withId(R.id.rcvList)).check(matches(not(isDisplayed())))
    }

    @Test
    fun downloadFileCheckRecyclerViewHasItem(){
        val result=runBlocking {
            repository.downloadFile("https://raw.githubusercontent.com/RabobankDev/AssignmentCSV/main/issues.csv")
        }
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        (result as Result.Success)
        onView(withId(R.id.rcvList)).check(matches(not(isDisplayed())))
    }
}