package com.example.movieapp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.net.URL


class MoviesAdapter(val movies: ArrayList<Movies>,val context: Context): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.listt_movies_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        holder.movieName.text = movies[position].constructMovieName
        holder.movieLongName.text = movies[position].constructMovietitleOriginal
        holder.movieDetail.text = movies[position].constructMovieDetail

        Picasso.with(context)
            .load("https://image.tmdb.org/t/p/w500/${movies[position].constructMoviePoster}")
            .into(holder.moviePoster)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val movieName: TextView = itemView.findViewById(R.id.textViewName)
        val movieLongName: TextView = itemView.findViewById(R.id.textViewOriginalTitle)
        val movieDetail: TextView = itemView.findViewById(R.id.textViewOverView)
        val moviePoster: ImageView = itemView.findViewById(R.id.imageViewPoster)

    }
}