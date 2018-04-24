package com.example.rest

import com.example.gred.infrastructure.rest.inventorypositionmovement.BadRequest
import org.jooby.Err
import org.jooby.Err.Missing
import org.jooby.Result
import org.jooby.Results
import reactor.core.publisher.Mono

internal fun handleException(e: Throwable, apiCodes: Map<Int, String>): Mono<Result> = Mono.fromCallable {
  when (e) {
// Jooby.Err
    is Err -> {
      when (e) {
      // Jooby.Err$Missing
        is Missing -> Results.with(BadRequest(215002, e.message!!), 400)
        else -> Results.with(BadRequest(215001, apiCodes.getOrDefault(215001, "Unknown")), 400)
      }
    }

// All other errors
    else -> Results.with(BadRequest(100017, apiCodes.getOrDefault(215001, "Unknown")), 400)
  }
}
