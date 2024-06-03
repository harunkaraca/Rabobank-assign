package com.example.rabobankassignment.Downloader

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FileDownloader internal constructor(private val downloadManager: DownloadManager,private val context: Context,private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):Downloader {
    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(context.filesDir.path, "downloaded.csv")
        return downloadManager.enqueue(request)
    }
}