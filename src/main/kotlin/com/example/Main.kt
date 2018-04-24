package com.example

import com.google.inject.Key
import com.example.infrastructure.config.ConfigModule
import com.example.infrastructure.kafka.KafkaModule
import com.example.infrastructure.kafka.MovementWriteRepository
import com.example.movie.domain.MovementService
import com.example.rest.HealthController
import com.example.rest.Reactor
import com.example.rest.RestController
import org.jooby.json.Jackson
import org.jooby.run
import org.pmw.tinylog.Configurator
import com.example.movie.domain.api.MovementService as MovementServiceApi
import com.example.movie.domain.api.MovementWriteRepository as MovementWriteRepositoryApi

fun main(vararg args: String) {
  run(*args) {
    use(Jackson())
    use(Reactor())
    use(ConfigModule())
    use(KafkaModule())
    use(HealthController())
    use(RestController())

    // Service bindings
    use { _, _, binder ->
      binder.bind(Key.get(MovementServiceApi::class.java)).to(MovementService::class.java)
      binder.bind(Key.get(MovementWriteRepositoryApi::class.java)).to(MovementWriteRepository::class.java)
    }

    onStop { _ -> Configurator.shutdownWritingThread(true) }
  }
}
