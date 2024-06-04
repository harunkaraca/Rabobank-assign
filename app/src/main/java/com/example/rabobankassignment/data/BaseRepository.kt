package com.example.rabobankassignment.data

import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.Result
interface BaseRepository {
    suspend fun downloadFile(url:String):Result<List<Issue>>
}