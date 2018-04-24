package com.example.movie.domain.api

import com.example.movie.domain.Movement
import com.example.movie.domain.MovementId
import reactor.core.publisher.Mono

/**
 * Movement Domain Service
 */
interface MovementService {
  /**
   * Generate a movement ID
   */
  fun createId(): Mono<MovementId>

  /**
   * Create a new movement
   */
  fun create(movement: Movement): Mono<Movement>
}
