package com.example.movieapp.Activities

import Json4Kotlin_Base_Movies
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Interfaces.GetMovieList
import com.example.movieapp.Movies
import com.example.movieapp.MoviesAdapter
import com.example.movieapp.R
import com.example.movieapp.Retrofit.RetrofitMoviesClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movies : ArrayList<Movies> = ArrayList()
        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager = LinearLayoutManager(this)


        val buttonForSearch = findViewById<Button>(R.id.buttonSearch)
        buttonForSearch.setOnClickListener{
            val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
            val dataFlight = service?.getAllData(editTextMovieName.text.toString())
            dataFlight?.enqueue(object: Callback<Json4Kotlin_Base_Movies> {

                override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error parsing json", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<Json4Kotlin_Base_Movies>,
                    response: Response<Json4Kotlin_Base_Movies>
                ) {
                    val body = response.body()

                    for (element in body!!.results){
                        movies.add(Movies(element.title,element.original_title,element.overview,element.poster_path))
                        Toast.makeText(applicationContext,element.title,Toast.LENGTH_LONG).show()
                    }

                    if (movies.size == 0){
                        Toast.makeText(applicationContext,"No data to be shown", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = MoviesAdapter(movies,applicationContext)


                }

            })
        }
    }
}
