package com.example.rabobankassignment.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rabobankassignment.Downloader.FileDownloader
import com.example.rabobankassignment.data.model.Issue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.rabobankassignment.Event
import com.example.rabobankassignment.data.BaseRepository
import com.example.rabobankassignment.data.Repository
import kotlinx.coroutines.launch
import com.example.rabobankassignment.data.model.Result

@HiltViewModel
class MainViewModel@Inject constructor(private val repository: BaseRepository):ViewModel() {

    private val _items = MutableLiveData<List<Issue>>().apply { value = emptyList() }
    val items: LiveData<List<Issue>> = _items
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError = _isDataLoadingError

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText
    init {

    }
    fun showSnackbarMessage(message: String) {
        _snackbarText.value = Event(message)
    }
    fun downloadFile(url:String){
        _dataLoading.value = true
        viewModelScope.launch {
            val result=repository.downloadFile(url)
            if(result is Result.Success){
                _isDataLoadingError.value = false
                _items.value=result.data
            }else if(result is Result.Error){
                _isDataLoadingError.value = true
                result.exception.message?.let { showSnackbarMessage(it) }
            }else{
                _isDataLoadingError.value = false
                _items.value = emptyList()
                showSnackbarMessage("Illegal state")
            }
            _dataLoading.value = false
        }
    }
}