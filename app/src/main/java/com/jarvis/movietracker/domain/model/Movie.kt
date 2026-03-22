package com.jarvis.movietracker.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String?,
    val popularity: Double,
    val originalLanguage: String?
)

