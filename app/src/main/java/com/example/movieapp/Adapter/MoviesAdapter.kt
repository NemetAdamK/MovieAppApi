package com.example.movieapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.API.TrailerResponse
import com.example.movieapp.Activities.MainActivity
import com.example.movieapp.Fragments.DetailsDialogFragment
import com.example.movieapp.Interfaces.GetMovieList
import com.example.movieapp.Movies
import com.example.movieapp.R
import com.example.movieapp.Retrofit.RetrofitMoviesClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detailsdialog.*
import kotlinx.android.synthetic.main.detailsdialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MoviesAdapter(val movies: ArrayList<Movies>, val context: Context): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.listt_movies_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieName.text = movies[position].constructMovieName
        holder.movieLongName.text = movies[position].constructMovieTitleOriginal
        holder.movieDetail.text = movies[position].constructMovieDetail


        holder.dateText.text = "Release date: " + movies[position].constructorReleaseDate
        holder.item.setOnClickListener {
            Toast.makeText(
                context,
                movies[position].constructMovieName + ", Movie clicked",
                Toast.LENGTH_SHORT
            ).show()
            showCreateCategoryDialog(movies[position])
        }

        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movies[position].constructMoviePoster}")
                .into(holder.moviePoster)
        } catch (e: Exception) {
            holder.moviePoster.setImageDrawable(null)
        }

    }

    fun addData(listItems: ArrayList<Movies>) {
        var size = listItems.size
        listItems.addAll(listItems)
        var sizeNew = listItems.size
        notifyItemRangeChanged(size, sizeNew)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val movieName: TextView = itemView.findViewById(R.id.textViewName)
        val movieLongName: TextView = itemView.findViewById(R.id.textViewOriginalTitle)
        val movieDetail: TextView = itemView.findViewById(R.id.textViewOverView)
        val moviePoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val dateText: TextView = itemView.findViewById(R.id.textViewDate)
        val item: LinearLayout = itemView.findViewById(R.id.linerLayout)

    }

    fun showCreateCategoryDialog(movie:Movies) {

        var key = "0"
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.detailsdialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle(movie.constructMovieName)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mAlertDialog.fullscreen_dialog_close.setOnClickListener {
            mAlertDialog.dismiss()
        }

        //mDialogView.descriptionTextView.text = movie.constructMovieDetail


        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        Log.v("BODY",movie.constructorMovieId.toString())
        val dataFlight = service?.getTrailer(movie.constructorMovieId)

        dataFlight?.enqueue(object : Callback<TrailerResponse> {
            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                Toast.makeText(context, "This film don't have trailer!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                val body = response.body()
                Log.v("BODY",body.toString())
                for (element in body!!.results) {
                    key = element.key
                    Log.v("Key",key)
                    mDialogView.webView.webViewClient = object : WebViewClient() {}
                    mDialogView.webView.settings.javaScriptEnabled = true
                    mDialogView.webView.loadUrl("https://www.youtube.com/watch?v=" + key)
                    Log.v("Link","https://www.youtube.com/watch?v=" + key)

                    }

            }

        })

        //login button click of custom layout

    }
}