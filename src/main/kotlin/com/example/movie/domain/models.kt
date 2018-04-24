package com.example.movie.domain

enum class Rating { G, PG, PG13, R }

data class Movie(val name: String, val rating: Rating, val releaseDate: String)

// Events
