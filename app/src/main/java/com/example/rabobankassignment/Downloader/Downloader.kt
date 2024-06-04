package com.example.rabobankassignment.Downloader
import com.example.rabobankassignment.data.model.Result
import java.net.URI

interface Downloader {
    suspend fun downloadFile(url: String): Result<String>
}