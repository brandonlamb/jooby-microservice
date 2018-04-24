package com.example.rest

import com.example.gred.infrastructure.rest.inventorypositionmovement.Movement
import com.example.movie.domain.Movement as DomainMovement
import com.example.movie.domain.State as DomainState

internal data class ApiError(val code: Int, val message: String)
internal data class Health(val message: String, val timestamp: String)

enum class Rating { G, PG, PG13, R }

data class Movie(val name: String, val rating: Rating, val releaseDate: String)
