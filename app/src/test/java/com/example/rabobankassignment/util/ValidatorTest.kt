package com.example.rabobankassignment.util

import com.example.rabobankassignment.data.model.Result
import com.example.rabobankassignment.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class ValidatorTest{
    @Test
    fun `Missing data in line`(){
        var array= mutableListOf<String>("Harun","Karaca","2")
        val result= Validator.checkArrayValidForImportedCSV(array.toTypedArray())
        (result as Result.Error)
        assertThat(result.exception.message).isEqualTo("Missing data in line")
    }

    @Test
    fun `Issue count is not digit`(){
        var array= mutableListOf<String>("Harun","Karaca","2a","1992-01-12","avatar")
        val result= Validator.checkArrayValidForImportedCSV(array.toTypedArray())
        (result as Result.Error)
        assertThat(result.exception.message).isEqualTo("Digit expected but string found")
    }

    @Test
    fun `Wrong time format`(){
        var array= mutableListOf<String>("Harun","Karaca","2","199201-12","avatar")
        val result= Validator.checkArrayValidForImportedCSV(array.toTypedArray())
        (result as Result.Error)
        assertThat(result.exception.message).isEqualTo("Date format is wrong")
    }

    @Test
    fun `Should succeded`(){
        var array= mutableListOf<String>("Harun","Karaca","2","1978-01-02T00:00:00","https://api.multiavatar.com/2cdf5db9b4dee297b7.png")
        val result= Validator.checkArrayValidForImportedCSV(array.toTypedArray())
        (result as Result.Success)
    }
}