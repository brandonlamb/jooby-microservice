package com.example.movie.domain.api

import com.example.movie.domain.Movement
import reactor.core.publisher.Mono

/**
 * Movement Write Infrastructure Service
 */
interface MovieWriteRepository {
  /**
   * Create an inventory position movement
   */
  fun create(movement: Movement): Mono<Void>
}
