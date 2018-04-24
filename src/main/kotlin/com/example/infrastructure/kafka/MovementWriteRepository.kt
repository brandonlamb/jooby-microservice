package com.example.infrastructure.kafka

import com.google.common.util.concurrent.Futures
import com.example.infrastructure.config.DefaultConfig
import com.example.gred.infrastructure.kafka.inventorypositionmovement.Metadata
import com.example.gred.infrastructure.kafka.inventorypositionmovement.MoveInventoryPosition
import com.example.movie.domain.Movement
import com.example.movie.domain.api.MovementWriteRepository
import org.apache.kafka.clients.producer.ProducerRecord
import org.pmw.tinylog.Logger
import reactor.core.publisher.Mono
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import com.example.gred.infrastructure.kafka.inventorypositionmovement.Movement as MovementProto
import com.example.gred.infrastructure.kafka.inventorypositionmovement.State as StateProto

@Singleton
class MovementWriteRepository @Inject constructor(
  defaultConfig: DefaultConfig,
  producers: Producers
) : MovementWriteRepository {
  private val config = defaultConfig.kafkaProducerConfig
  private val kafkaProducer = producers.movement

  init {
    Logger.info("KafkaMovementWriteRepository started")
  }

  override fun create(movement: Movement): Mono<Void> = Mono.create { monoSink ->
    Mono
      .just(movement)
      .map {
        MoveInventoryPosition
          .Builder()
          .metadata(Metadata.Builder().correlationId(UUID.randomUUID().toString()).tenantId(config.tenantId).build())
          .data(MovementProto(
            it.locationNumber,
            it.itemNumber,
            it.fromState?.convert(),
            it.toState?.convert(),
            it.quantity,
            it.override,
            it.timestamp.toString(),
            it.id
          ))
          .build()
      }
      .map {
        val future = kafkaProducer.send(ProducerRecord(config.topic, it.metadata, it)) { _, e ->
          if (e == null) {
            Logger.info(
              "correlationId={}, locationNumber={}, itemNumber={}, fromState={}, toState={}, override={}, timestamp={}",
              it.metadata.correlationId,
              it.data.locationNumber,
              it.data.itemNumber,
              it.data.fromState,
              it.data.toState,
              it.data.override,
              it.data.timestamp
            )
          } else {
            Logger.error(
              "correlationId={}, locationNumber={}, itemNumber={}, fromState={}, toState={}, override={}, timestamp={}, message={}",
              it.metadata.correlationId,
              it.data.locationNumber,
              it.data.itemNumber,
              it.data.fromState,
              it.data.toState,
              it.data.override,
              it.data.timestamp,
              e.message
            )
          }
        }

        Futures.lazyTransform(future, { monoSink.success() })
      }
  }
}
