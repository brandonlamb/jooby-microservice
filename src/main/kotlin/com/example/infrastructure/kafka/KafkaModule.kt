package com.example.infrastructure.kafka

import akka.actor.ActorSystem
import akka.japi.function.Function
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import akka.stream.Supervision
import com.google.inject.Binder
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config
import org.apache.kafka.common.KafkaException
import org.jooby.Env
import org.jooby.Jooby
import org.pmw.tinylog.Logger
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.net.ProtocolException
import java.util.concurrent.TimeUnit

class KafkaModule : Jooby.Module {
  override fun configure(env: Env, conf: Config, binder: Binder) {
    val actorSystem = ActorSystem.create("system")

    val actorMaterializer = ActorMaterializer.create(
      ActorMaterializerSettings
        .create(actorSystem)
        .withSupervisionStrategy(Function {
          Logger.error(it)

          when (it) {
          // unrecoverable error
            is KafkaException -> Supervision.resume()
            is ProtocolException -> Supervision.resume()

          // unknown reason, we better not to drop any message
            else -> Supervision.restart()
          }
        }),
      actorSystem
    )

    val mapper = ObjectMapper()

    val producerSettings: ProducerSettings<Metadata, CreateMovie> = ProducerSettings.create(
      actorSystem,
      MetadataAdapter(mapper),
      CreateMovieAdapter(mapper)
    )

    binder.bind(ActorSystem::class.java).toInstance(actorSystem)
    binder.bind(ActorMaterializer::class.java).toInstance(actorMaterializer)
    binder.bind(Producers::class.java).toInstance(Producers(producerSettings.createKafkaProducer()))

    env.onStop { r ->
      r.require(ActorMaterializer::class.java).shutdown()
      Await.result(r.require(ActorSystem::class.java).terminate(), Duration.create(30, TimeUnit.SECONDS))
    }

    env.onStop { r ->
      val producer = r.require(Producers::class.java)
      producer.movement.flush()
      producer.movement.close()
    }
  }
}
