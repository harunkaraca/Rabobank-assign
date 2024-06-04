package com.example.rabobankassignment.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.Result
import kotlinx.coroutines.runBlocking
import java.util.LinkedHashMap
import javax.inject.Inject

class FakeRepository @Inject constructor():BaseRepository {

    var datas: LinkedHashMap<String, Issue> = LinkedHashMap()

    private var shouldReturnError = false

    private val observableIssues = MutableLiveData<Result<List<Issue>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun downloadFile(url: String): Result<List<Issue>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(datas.values.toList())
    }
    @VisibleForTesting
    fun addIssues(vararg issues: Issue) {
        for (task in issues) {
            datas[task.firstName] = task
        }
        runBlocking { observableIssues.value=Result.Success(datas.values.toList()) }
    }
}