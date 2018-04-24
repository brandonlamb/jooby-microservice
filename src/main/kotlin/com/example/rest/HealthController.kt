package com.example.rest

import org.jooby.Kooby
import reactor.core.publisher.Mono
import java.time.LocalDateTime.now

class HealthController : Kooby({
  get("/health") {
    Mono.just(Health("pong", now().toString()))
  }
})
