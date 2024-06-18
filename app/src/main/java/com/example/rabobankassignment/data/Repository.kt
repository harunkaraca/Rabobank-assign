package com.example.rabobankassignment.data

import android.os.Environment
import android.util.Log
import com.example.rabobankassignment.Downloader.FileDownloader
import com.example.rabobankassignment.data.model.Headers
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.IssueDetail
import com.example.rabobankassignment.data.model.Result
import com.example.rabobankassignment.data.model.succeeded
import com.example.rabobankassignment.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import com.example.rabobankassignment.util.Validator

class Repository(
    private val fileDownloader: FileDownloader,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
):BaseRepository {
    private val TAG="Repository"
    override suspend fun downloadFile(url:String): Result<Issue> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher){
                val result=fileDownloader.downloadFile(url)
                (result as? Result.Success)?.let {
                    val data=fetchDataFromFile(result.data)
                    (data as? Result.Success)?.let {
                        return@withContext Result.Success(it.data)
                    }
                    return@withContext data as Result.Error
                }
                return@withContext Result.Error(Exception("Illegal state"))
            }
        }
    }

    private fun fetchDataFromFile(filePath:String): Result<Issue> {

        val file= File(filePath)
        val inputStream: InputStream = FileInputStream(file)
        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            var csvLine=""
            lateinit var headers:Headers
            reader.readLine()?.also {
                csvLine = it
                val row = csvLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val result= Validator.checkHeaderArrayValidForImportedCSV(row)
                if(result is Result.Success){
                    headers=Headers(
                        "${row[0].replace("\"","")} :",
                        "${row[1].replace("\"","")} :",
                        "${row[2].replace("\"","")} :",
                        "${row[3].replace("\"","")} :")
                }else{
                    return result as Result.Error
                }
            }
            val issueDetails:MutableList<IssueDetail> = mutableListOf()
            while ((reader.readLine()?.also { csvLine = it }) != null) {
                val row = csvLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val result= Validator.checkArrayValidForImportedCSV(row)
                if(result is Result.Success){
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val localDateTime = LocalDateTime.parse(row[3].replace("\"",""), formatter)
                    val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
                    val issue=IssueDetail(row[0].replace("\"",""),
                        row[1].replace("\"",""),
                        row[2].toInt(),
                        date,
                        row[4].replace("\"",""))
                    issueDetails.add(issue)
                }else{
                    return result as Result.Error
                }
            }
            inputStream.close()
            var resultList= Issue(headers,issueDetails)
            return Result.Success(resultList)
        } catch (ex: Exception) {
            inputStream.close()
            Timber.tag(TAG).e("Error in fetchDataFromFile ${ex.message}")
            return Result.Error(Exception("Error while reading CSV file"))
        }


    }
}