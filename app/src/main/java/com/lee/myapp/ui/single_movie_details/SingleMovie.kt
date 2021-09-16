package com.lee.myapp.ui.single_movie_details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.lee.myapp.R
import com.lee.myapp.data.api.POSTER_BASE_URL
import com.lee.myapp.data.api.TheMovieDBClient
import com.lee.myapp.data.api.TheMovieDBInterface
import com.lee.myapp.data.repository.NetworkState
import com.lee.myapp.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*

class SingleMovie: AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)


        // 옵저빙 설정
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        // 옵저빙 설정
        viewModel.networkState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.progress_bar).visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.txt_error).visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetails) {
        // movie
        findViewById<TextView>(R.id.movie_title).text = it.title
        findViewById<TextView>(R.id.movie_tagline).text = it.tagline
        findViewById<TextView>(R.id.movie_release_date).text = it.tagline
        findViewById<TextView>(R.id.movie_rating).text = it.rating.toString()
        findViewById<TextView>(R.id.movie_runtime).text = it.runtime.toString() + "minutes"
        findViewById<TextView>(R.id.movie_overview).text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        findViewById<TextView>(R.id.movie_budget).text = formatCurrency.format(it.budget)
        findViewById<TextView>(R.id.movie_revenue).text = formatCurrency.format(it.revenue)

        // 이미지 바인딩
        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into( findViewById<ImageView>(R.id.iv_movie_poster));

    }


    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}