package com.lee.myapp.view

import androidx.lifecycle.LiveData
import com.lee.myapp.data.api.TheMovieDBInterface
import com.lee.myapp.data.repository.MovieDetailsNetworkDataSource
import com.lee.myapp.data.repository.NetworkState
import com.lee.myapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }


}