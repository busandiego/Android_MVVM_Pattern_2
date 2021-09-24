 package com.lee.myapp.ui.popular_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.myapp.R
import com.lee.myapp.data.api.TheMovieDBClient
import com.lee.myapp.data.api.TheMovieDBInterface
import com.lee.myapp.data.repository.NetworkState

import com.lee.myapp.ui.single_movie_details.SingleMovie
import com.lee.myapp.ui.single_movie_details.SingleMovieViewModel

 class MainActivity : AppCompatActivity() {

     private lateinit var viewModel: MainActivityViewModel

     lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType:Int = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1          // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3
            }
        }

        findViewById<RecyclerView>(R.id.rv_movie_list).layoutManager = gridLayoutManager
        findViewById<RecyclerView>(R.id.rv_movie_list).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.rv_movie_list).adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        // TODO 프로그레스바 적용 참조할것.
        viewModel.networkState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.progress_bar_popular).visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            findViewById<TextView>(R.id.text_error_popular).visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })






    }

     private fun getViewModel(): MainActivityViewModel {
         return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
             override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                 @Suppress("UNCHECKED_CAST")
                 return MainActivityViewModel(movieRepository) as T
             }
         })[MainActivityViewModel::class.java]
     }

 }