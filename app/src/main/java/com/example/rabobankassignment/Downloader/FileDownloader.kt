package com.example.rabobankassignment.Downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.rabobankassignment.data.model.Result
import com.example.rabobankassignment.util.fileName
import timber.log.Timber
import java.io.File
import java.net.URI

class FileDownloader internal constructor(private val downloadManager: DownloadManager,private val context: Context,private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):Downloader {
    private val TAG="FileDownloader"
    override suspend fun downloadFile(url: String): Result<String> {
           try{
               val request = DownloadManager.Request(url.toUri())
                   .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                   .setDestinationInExternalPublicDir(
                       Environment.DIRECTORY_DOWNLOADS,
                       url.fileName())
               downloadManager.enqueue(request)
               return Result.Success("${Environment.DIRECTORY_DOWNLOADS}/${url.fileName()}")
           }
            catch (exception: Exception) {
                Timber.tag(TAG).e(exception, "Failed to download " + url + "!")
                return Result.Error(Exception("Failed to find downloaded file at ${url}"))
            }
        }
}