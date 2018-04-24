package com.example.movie.domain

import com.example.movie.domain.api.MovementService
import com.example.movie.domain.api.MovementWriteRepository
import reactor.core.publisher.Mono
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovementService @Inject constructor(
  private val movementWriteRepository: MovementWriteRepository
) : MovementService {
  override fun createId(): Mono<MovementId> = Mono.just(UUID.randomUUID().toString())

  override fun create(movement: Movement): Mono<Movement> = createId()
    .map { movement.copy(id = it) }
    .zipWhen { movementWriteRepository.create(movement) }
    .map { it.t1 }
    .doOnError {
      when (it) {
        is CreateMovementException -> throw it
      }
    }
}
