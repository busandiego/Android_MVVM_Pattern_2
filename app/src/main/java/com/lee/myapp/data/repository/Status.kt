package com.lee.myapp.data.repository

enum class Status {
    SUCCESS,
    RUNNING,
    FAILED
}
// https://developers.themoviedb.org/3
// key: 320e3775e7cc9acdd43aeda6b028a4a3

class NetworkState(val status: Status, val msg: String) {

    // use companion object to be static
    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
        }

    }

}