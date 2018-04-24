package com.example.movie.domain

import java.time.OffsetDateTime

typealias MovementId = String
typealias PositionId = String
typealias LocationNumber = Int
typealias ItemNumber = Long

enum class State { FROZEN, THAWING, AMBIENT, UNKNOWN }

data class Movement(
  val id: MovementId?,
  val locationNumber: LocationNumber,
  val itemNumber: ItemNumber,
  val fromState: State?,
  val toState: State?,
  val quantity: Int,
  val override: Boolean,
  val timestamp: OffsetDateTime
)

// Events

data class MovementCreated(val movement: Movement)
data class CreateMovementFailed(val movement: Movement, val e: Exception)
data class DuplicateMovementFound(val movement: Movement)
data class MovementValidationFailed(val movement: Movement, val e: Exception)
