package com.example.rabobankassignment.util

import com.example.rabobankassignment.data.model.Result
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object Validator {
    fun checkArrayValidForImportedCSV(array:Array<String>): Result<String> {
        if(array.size!=5){
            return Result.Error(Exception("Missing data in line"))
        }
        if(!array[2].all { char -> char.isDigit() }){
            return Result.Error(Exception("Digit expected but string found"))
        }
        try {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val localDateTime = LocalDateTime.parse(array[3].replace("\"",""), formatter)
            val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }catch (ex:Exception){
            return Result.Error(Exception("Date format is wrong"))
        }
        return Result.Success("")
    }
    fun checkHeaderArrayValidForImportedCSV(array:Array<String>): Result<String> {
        if(array.size!=5){
            return Result.Error(Exception("Missing data in line"))
        }
        return Result.Success("")
    }
}