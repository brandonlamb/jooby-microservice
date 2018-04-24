package com.example.movie.domain

class CreateMovementException(e: Exception) : Exception(e)
class InvalidMovementStateException(message: String) : Exception(message)
class InvalidMovementQuantity(message: String) : Exception(message)
class CreatePositionException(e: Exception) : Exception(e)
class UpdatePositionException(e: Exception) : Exception(e)
class CreateAuditException(e: Exception) : Exception(e)
