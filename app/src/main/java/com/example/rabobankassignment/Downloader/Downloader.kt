package com.example.rabobankassignment.Downloader

interface Downloader {
    fun downloadFile(url: String): Long
}