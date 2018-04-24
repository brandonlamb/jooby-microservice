package com.example.infrastructure.akka

import akka.actor.ActorSystem
import akka.japi.function.Function
import akka.stream.ActorMaterializer
import akka.stream.ActorMaterializerSettings
import akka.stream.Supervision
import com.google.inject.Binder
import com.typesafe.config.Config
import org.apache.kafka.common.KafkaException
import org.jooby.Env
import org.jooby.Jooby
import org.pmw.tinylog.Logger
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.net.ProtocolException
import java.util.concurrent.TimeUnit

/**
 * CDI producer which creates the akka system and materializer
 */
class AkkaModule : Jooby.Module {
  init {
    Logger.info("AkkaModule started")
  }

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

    binder.bind(ActorSystem::class.java).toInstance(actorSystem)
    binder.bind(ActorMaterializer::class.java).toInstance(actorMaterializer)

    env.onStop { r ->
      val system = r.require(ActorSystem::class.java)
      Await.result(system.terminate(), Duration.create(30, TimeUnit.SECONDS))
    }
  }
}
