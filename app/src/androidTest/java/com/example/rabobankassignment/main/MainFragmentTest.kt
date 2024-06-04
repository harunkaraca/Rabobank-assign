package com.example.rabobankassignment.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.rabobankassignment.R
import com.example.rabobankassignment.launchFragmentInHiltContainer
import com.example.rabobankassignment.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
    @Test
    fun wrongUrlShouldShowSnackBar(){
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        onView(withId(R.id.etUrl)).perform(clearText(), typeText("dka"));
        onView(withId(R.id.btnDownload)).perform(click())
        onView(withId(R.id.tvError)).check(matches((isDisplayed())))
        onView(withId(com.google.android.material.R.id.snackbar_text)) .check(matches(withText("Illegal state")))
    }
    @Test
    fun emptyUrlShouldShowSnackBar(){
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        onView(withId(R.id.etUrl)).perform(clearText(), typeText(""));
        onView(withId(R.id.btnDownload)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text)) .check(matches(withText(R.string.url_can_not_be_empty)))
    }

    @Test
    fun noDownloadedFileInvisibleRecyclerView(){
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        onView(withId(R.id.rcvList)).check(matches(not(isDisplayed())))
    }
    @Test
    fun downloadFileCheckRecyclerViewHasItems(){
        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_RaboBankAssignment)
        onView(withId(R.id.etUrl)).perform(clearText(), typeText("https://raw.githubusercontent.com/RabobankDev/AssignmentCSV/main/issues.csv"));
        onView(withId(R.id.btnDownload)).perform(click())
        runBlocking {
            delay(1000L)
        }
        onView(withId(R.id.rcvList)).check(matches((isDisplayed())))
        onView(withId(R.id.rcvList)).check(matches(hasChildCount(3)))
    }
}