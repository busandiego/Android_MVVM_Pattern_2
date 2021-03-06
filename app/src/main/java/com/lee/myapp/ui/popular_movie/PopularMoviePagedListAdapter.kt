package com.lee.myapp.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.myapp.R
import com.lee.myapp.data.api.POSTER_BASE_URL
import com.lee.myapp.data.repository.NetworkState
import com.lee.myapp.data.vo.Movie
import com.lee.myapp.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_single_movie.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

private const val TAG = "PopularMoviePagedListAd"

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {


    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent,false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)

        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState !== null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?, context: Context) {
          //  itemView.cv_movie_title
            itemView.cv_movie_title.text = movie?.title


            Log.d(TAG, "bind: >>>>>>>>> ${movie?.title}")
            Log.d(TAG, "bind: >>>>>>>>> ${movie?.releaseDate}")
            // itemView.movie_release_date.text = movie?.releaseDate
           // itemView.movie_release_date.text = movie?.title

            val moviePosterURL: String = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.findViewById<ImageView>(R.id.cv_iv_movie_poster))


            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {

            if (networkState !== null && networkState == NetworkState.LOADING) {
                itemView.findViewById<ProgressBar>(R.id.progress_bar_item).visibility =
                    View.VISIBLE
            } else {
                itemView.findViewById<ProgressBar>(R.id.progress_bar_item).visibility = View.GONE

            }


        }


    }

    fun setNetworkState(newNetworkSate: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkSate
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                              // hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())     // remove the progressbar at the end
            } else {                                        // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())    // add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkSate) {    // hasExtraRow is true and hadExtraRow true
            notifyItemChanged(itemCount - 1)
        }

    }

}