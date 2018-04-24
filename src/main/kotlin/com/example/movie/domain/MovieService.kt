package com.example.movie.domain

import com.example.movie.domain.api.MovieService
import com.example.movie.domain.api.MovieWriteRepository
import reactor.core.publisher.Mono
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieService @Inject constructor(
  private val movieWriteRepository: MovieWriteRepository
) : MovieService {
  override fun createId(): Mono<MovementId> = Mono.just(UUID.randomUUID().toString())

  override fun create(movement: Movement): Mono<Movement> = createId()
    .map { movement.copy(id = it) }
    .zipWhen { movieWriteRepository.create(movement) }
    .map { it.t1 }
    .doOnError {
      when (it) {
        is CreateMovementException -> throw it
      }
    }
}
