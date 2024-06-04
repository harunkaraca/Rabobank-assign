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
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class FileDownloader internal constructor(private val context: Context,private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):Downloader {
    private val TAG="FileDownloader"
    override suspend fun downloadFile(url: String): Result<String> {
           try{
               val result=performFileDownload(url)
               (result as? Result.Success)?.let {
                   return Result.Success(result.data)
               }
               return result as Result.Error
           }
            catch (exception: Exception) {
                Timber.tag(TAG).e(exception, "Failed to download " + url + "!")
                return Result.Error(Exception("Failed to find downloaded file at ${url}"))
            }
    }

    private fun performFileDownload(fileUrl: String): Result<String> {
        try {
            val downloadTarget = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileUrl.fileName())
            val connection = URL(fileUrl).openConnection() as HttpURLConnection
            val contentLength = connection.contentLength
            val inputStream = BufferedInputStream(connection.inputStream)
            val outputStream = FileOutputStream(downloadTarget.path)
            val buffer = ByteArray(4096)
            var downloadedFileSize = 0L
            var currentRead = 0
            while (currentRead != -1) {
                downloadedFileSize += currentRead
                outputStream.write(buffer, 0, currentRead)
                currentRead = inputStream.read(buffer, 0, buffer.size)
            }
            outputStream.close()
            inputStream.close()
            return Result.Success(downloadTarget.path)
        }catch (exception:Exception){
            Timber.tag(TAG).e(exception, "Failed to download " + fileUrl + "!")
            return Result.Error(Exception("Failed to find downloaded file at ${fileUrl}"))
        }
    }

}