package com.example.rabobankassignment.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.Result
import kotlinx.coroutines.runBlocking
import java.util.Date
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
        val issue1 = Issue("Name1", "Surname1",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        val issue2 = Issue("Name2", "Surname2",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        val issue3 = Issue("Name3", "Surname3",1, Date(),"https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        datas[issue1.firstName]=issue1
        datas[issue2.firstName]=issue2
        datas[issue3.firstName]=issue3
        return Result.Success(datas.values.toList())
    }
    private fun addIssues(vararg issues: Issue) {
        for (task in issues) {
            datas[task.firstName] = task
        }
        runBlocking { observableIssues.value=Result.Success(datas.values.toList()) }
    }
}