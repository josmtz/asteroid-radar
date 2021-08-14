package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application){

//    private val database = getInstance(application)
//    private val asteroidRepository = AsteroidRepository(database)

    /**
     * A list of Asteroids that can be shown on the screen. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()


    /**
     * A playlist of videos that can be shown on the screen. Views should use this to get access
     * to the data.
     */
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay
//
////    val asteroid = asteroidRepository.allAsteroids
//
    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
//        refreshDataFromNetwork()
        viewModelScope.launch {
            updatePictureOfDay()
//            asteroidRepository.refreshAsteroids()
        }
    }
//    /**
//     * Refresh data from network and pass it via LiveData. Use a coroutine launch to get to
//     * background thread.
//     */
////    private fun refreshDataFromNetwork() = viewModelScope.launch {
////        try {
////            val asteroids = AsteroidApi.retrofitService.getAsteroids().await()
////            _asteroids.postValue(asteroids.asDomainModel())
////        } catch (networkError: IOException) {
////            // Show an infinite loading spinner if the request fails
////            // challenge exercise: show an error to the user if the network request fails
////        }
////    }
//
//
//
//
    private suspend fun updatePictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                _pictureOfDay.postValue(
                    AsteroidApiService.AsteroidApi.retrofitService.getPictureOfDay(API_KEY)
                )
            } catch (err: Exception) {
                Log.e("updatePictureOfDay", err.printStackTrace().toString())
            }
        }

    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class MainViewModelFactory(private val repository: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}


