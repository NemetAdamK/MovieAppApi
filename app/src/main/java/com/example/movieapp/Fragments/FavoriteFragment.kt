package com.example.movieapp.Fragments

import Json4Kotlin_Base_Movies
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Adapter.MoviesAdapter
import com.example.movieapp.Interfaces.GetMovieList
import com.example.movieapp.Interfaces.GetTopMovieList
import com.example.movieapp.Movies
import com.example.movieapp.R
import com.example.movieapp.Retrofit.RetrofitMoviesClient
import com.example.movieapp.userId
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.favoritefragment.*
import kotlinx.android.synthetic.main.homefragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment() {

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
        inflater.inflate(R.layout.favoritefragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        recyclerViewFavorites.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        recyclerViewFavorites.layoutManager = LinearLayoutManager(context)

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        var myLayoutManager = LinearLayoutManager(context)
        recyclerViewFavorites?.addOnScrollListener(object : PaginationScrollListener(myLayoutManager) {
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


        val auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("users").child(userId).child("movies").orderByKey().addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (element in p0.children){
                    val QuestionResult = element.getValue(Movies::class.java)
                    movies.add(Movies(QuestionResult!!.constructMovieName,
                        QuestionResult.constructMovieTitleOriginal
                        ,QuestionResult.constructMovieDetail
                        ,QuestionResult.constructMoviePoster
                        ,QuestionResult.constructorReleaseDate
                        ,QuestionResult.constructorMovieId,
                        QuestionResult.constructorIsFavorite))
                }

                if (movies.size == 0){
                    Toast.makeText(context,"No data to be shown", Toast.LENGTH_SHORT).show()
                }
                recyclerViewFavorites.adapter =
                    MoviesAdapter(
                        movies,
                        context!!
                    )
            }
        })
    }

    fun getMoreItems() {

        isLoading = false

        adapter.addData(movies)
    }



}
