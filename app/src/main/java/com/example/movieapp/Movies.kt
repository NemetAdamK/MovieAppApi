package com.example.movieapp

class Movies(_movieName: String,_movieOriginalName:String,_movieDetail:String,_picture: String?,_releaseDate: String?,_id: String,_isFavorite: Boolean) {

    var constructMovieName: String = ""
    var constructMovieDetail: String = ""
    var constructMoviePoster: String? = ""
    var constructMovieTitleOriginal: String = ""
    var constructorReleaseDate: String? = ""
    var constructorMovieId: String =" "
    var constructorIsFavorite: Boolean = false

    constructor() : this("No one","0","no question","no group","","",false)

    init{
        this.constructMovieName=_movieName
        this.constructMovieDetail=_movieDetail
        this.constructMoviePoster=_picture
        this.constructMovieTitleOriginal=_movieOriginalName

    }

    init{
        this.constructMovieName=_movieName
        this.constructMovieDetail=_movieDetail
        this.constructMoviePoster=_picture
        this.constructMovieTitleOriginal=_movieOriginalName
        this.constructorReleaseDate=_releaseDate
        this.constructorMovieId=_id
    }



}