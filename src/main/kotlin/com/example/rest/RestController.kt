package com.example.rest

import com.example.infrastructure.config.DefaultConfig
import com.example.movie.domain.api.MovieService
import org.jooby.Kooby
import org.jooby.MediaType
import org.jooby.Results
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import com.example.movie.domain.Movement as DomainMovement

class RestController : Kooby({
  post("/") { req ->
    val apiCodes = require(DefaultConfig::class.java).apiCodes
    val movementService = require(MovieService::class.java)

    Mono.just(req.body().to(Movement::class.java))
      .map { it.toDomain() }
      .map { movementService.create(it) }
      .doOnError { handleException(it, apiCodes) }
      .subscribeOn(Schedulers.elastic())
      .map { Results.accepted().type(MediaType.json) }
  }
})
