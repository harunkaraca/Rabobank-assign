package com.example.rabobankassignment.data

import androidx.lifecycle.MutableLiveData
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.Result
import java.util.LinkedHashMap
import javax.inject.Inject

class FakeRepository @Inject constructor():BaseRepository {

    var serviceData: LinkedHashMap<String, Issue> = LinkedHashMap()

    private var shouldReturnError = false

    private val observableTasks = MutableLiveData<Result<List<Issue>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun downloadFile(url: String): Result<List<Issue>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(serviceData.values.toList())
    }
}