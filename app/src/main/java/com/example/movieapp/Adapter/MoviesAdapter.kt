package com.example.movieapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Movies
import com.example.movieapp.R
import com.squareup.picasso.Picasso
import java.lang.Exception


class MoviesAdapter(val movies: ArrayList<Movies>, val context: Context): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.listt_movies_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieName.text = movies[position].constructMovieName
        holder.movieLongName.text = movies[position].constructMovieTitleOriginal
        holder.movieDetail.text = movies[position].constructMovieDetail

        holder.item.setOnClickListener {
            Toast.makeText(context,movies[position].constructMovieName+ ", Movie clicked",Toast.LENGTH_SHORT).show()
        }

        try{
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movies[position].constructMoviePoster}")
                .into(holder.moviePoster)
        } catch (e: Exception){
            holder.moviePoster.setImageDrawable(null)
        }

    }

    fun addData(listItems: ArrayList<Movies>) {
        var size = listItems.size
        listItems.addAll(listItems)
        var sizeNew = listItems.size
        notifyItemRangeChanged(size, sizeNew)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val movieName: TextView = itemView.findViewById(R.id.textViewName)
        val movieLongName: TextView = itemView.findViewById(R.id.textViewOriginalTitle)
        val movieDetail: TextView = itemView.findViewById(R.id.textViewOverView)
        val moviePoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val item: LinearLayout = itemView.findViewById(R.id.linerLayout)

    }
}