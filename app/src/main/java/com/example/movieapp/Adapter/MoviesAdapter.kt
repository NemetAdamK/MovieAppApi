package com.example.movieapp.Adapter

import Json4Kotlin_Base_Movies
import android.app.ActionBar
import android.app.AlertDialog
import android.app.PendingIntent.getActivity
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.API.TrailerResponse
import com.example.movieapp.Interfaces.GetMovieList
import com.example.movieapp.Interfaces.GetTopMovieList
import com.example.movieapp.Movies
import com.example.movieapp.R
import com.example.movieapp.Retrofit.RetrofitMoviesClient
import com.example.movieapp.booleanIsFavorite
import com.example.movieapp.userId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detailsdialog.*
import kotlinx.android.synthetic.main.detailsdialog.view.*
import kotlinx.android.synthetic.main.favoritefragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            showCreateCategoryDialog(movies[position],holder)
        }

        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movies[position].constructMoviePoster}")
                .into(holder.moviePoster)
        } catch (e: Exception) {
            holder.moviePoster.setImageDrawable(null)
        }

    }

    private fun checkIfMovieFavorite(movie: Movies){
        val auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("users").child(userId).child("movies").orderByKey().addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (element in p0.children) {
                    val QuestionResult = element.getValue(Movies::class.java)
                    booleanIsFavorite = false;
                    try {
                        if (QuestionResult?.constructMovieName == movie.constructMovieName)
                            booleanIsFavorite = true

                    }catch (e:java.lang.Exception){

                    }
                }
            }
        })

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

    fun showCreateCategoryDialog(movie:Movies,holder:ViewHolder) {

        var key = "0"

        checkIfMovieFavorite(movie)
        val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.detailsdialog, null)
        //AlertDialogBuilder

        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle(movie.constructMovieName)
        //show dialog

        val mAlertDialog = mBuilder.show()

        val window = mAlertDialog.getWindow();
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        mDialogView.fullscreen_dialog_close.setOnClickListener {
            mAlertDialog.dismiss()
        }

        Log.v("Conn",movie.constructMovieDetail)
        try {
            Log.v("Conn","elotte")
            mDialogView.descriptionTextView.text = movie.constructMovieDetail
            Log.v("Conn","utanna")

        }catch (e:java.lang.Exception){
            mDialogView.descriptionTextView.text = "No detail"
        }


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


        mDialogView.recyclerView
        .addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        mDialogView.recyclerView.layoutManager = LinearLayoutManager(context)
        val service1 = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataSimilar = service1?.getSimilar(movie.constructorMovieId.toInt())
        Log.v("MovieId",movie.constructorMovieId)
        dataSimilar?.enqueue(object: Callback<Json4Kotlin_Base_Movies> {

            override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                Toast.makeText(context, "Error parsing json", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<Json4Kotlin_Base_Movies>,
                response: Response<Json4Kotlin_Base_Movies>
            ) {
                val moviesSimilar = ArrayList<Movies>()


                val body = response.body()
                Log.e("ERRORRRR",body.toString())
                for (element in body!!.results) {
                    Log.v("MovieId",movie.constructorMovieId)
                    moviesSimilar.add(
                        Movies(
                            element.title,
                            element.original_title,
                            element.overview,
                            element.poster_path,
                            element.release_date,
                            element.id.toString()
                        )
                    )
                }

                if (moviesSimilar.size == 0) {
                    Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
                }
                mDialogView.recyclerView.adapter =
                    MoviesAdapter(moviesSimilar, context!!)


            }
        })


        /*
        if (booleanIsFavorite){
            mDialogView.buttonAddToFav.text = "Remove from favorites"
        } else {
            mDialogView.buttonAddToFav.text = "Add to favorites"
        }

*/ // later feature

        mDialogView.buttonAddToFav.setOnClickListener {

            val auth = FirebaseAuth.getInstance()
            userId = auth.currentUser!!.uid
            FirebaseDatabase.getInstance().reference.child("users").child(userId).child("movies").orderByKey().addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var ok = 0
                    for (element in p0.children){
                        val QuestionResult = element.getValue(Movies::class.java)
                        if (QuestionResult?.constructMovieDetail == movie.constructMovieDetail){
                            ok=1

                        }
                    }
                    if (ok==0){
                        FirebaseDatabase.getInstance().getReference("users").child(userId).child("movies").push().setValue(movie)
                    } else {
                        Toast.makeText(context,"Movie already added",Toast.LENGTH_SHORT).show()
                    }

                }
            })

        }

        //login button click of custom layout

    }
}