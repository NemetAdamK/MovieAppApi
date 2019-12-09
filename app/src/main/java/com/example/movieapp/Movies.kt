package com.example.movieapp

class Movies(_movieName: String,_movieOriginalName:String,_movieDetail:String,_picture: String?) {

    var constructMovieName: String = ""
    var constructMovieDetail: String = ""
    var constructMoviePoster: String? = ""
    var constructMovieTitleOriginal: String = ""

    init{
        this.constructMovieName=_movieName
        this.constructMovieDetail=_movieDetail
        this.constructMoviePoster=_picture
        this.constructMovieTitleOriginal=_movieOriginalName
    }
}