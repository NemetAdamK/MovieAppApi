package com.example.movieapp.Fragments

import Json4Kotlin_Base_Movies
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Adapter.MoviesAdapter
import com.example.movieapp.Interfaces.GetNowPlaying
import com.example.movieapp.Movies
import com.example.movieapp.R
import com.example.movieapp.Retrofit.RetrofitMoviesClient
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.nowplayingfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NowPlayingFragment : Fragment() {

    val movies : ArrayList<Movies> = ArrayList()
    var page = 1
    var isLoading = false
    var limit = 10

    lateinit var adapter: MoviesAdapter


    abstract class PaginationScrollListener
    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
        (var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

        abstract fun isLastPage(): Boolean

        abstract fun isLoading(): Boolean

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading() && !isLastPage()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems()
                }//                    && totalItemCount >= ClothesFragment.itemsCount
            }
        }
        abstract fun loadMoreItems()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.nowplayingfragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        populateWithMovies()

        recyclerViewSearchNowMovies.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        recyclerViewSearchNowMovies.layoutManager = LinearLayoutManager(context)

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        var myLayoutManager = LinearLayoutManager(context)
        recyclerViewSearchNowMovies?.addOnScrollListener(object : PaginationScrollListener(myLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call loadmore items to get more data
                getMoreItems()
            }
        })




    }

    fun getMoreItems() {

        isLoading = false

        adapter.addData(movies)
    }

    private fun populateWithMovies() {
        recyclerViewSearchNowMovies.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        recyclerViewSearchNowMovies.layoutManager = LinearLayoutManager(context)
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetNowPlaying::class.java)
        val dataFlight = service?.getTopMovieData()
        dataFlight?.enqueue(object: Callback<Json4Kotlin_Base_Movies> {

            override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                Toast.makeText(context, "Error parsing json", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<Json4Kotlin_Base_Movies>,
                response: Response<Json4Kotlin_Base_Movies>
            ) {
                val body = response.body()
                Log.e("ERRORRRR",body.toString())
                for (element in body!!.results) {
                    movies.add(
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

                if (movies.size == 0) {
                    Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
                }
                recyclerViewSearchNowMovies.adapter =
                    MoviesAdapter(movies, context!!)


            }
        })
    }
}
