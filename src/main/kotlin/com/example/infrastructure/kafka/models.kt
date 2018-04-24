package com.example.infrastructure.kafka

import com.example.gred.infrastructure.kafka.inventorypositionmovement.Metadata
import com.example.gred.infrastructure.kafka.inventorypositionmovement.MoveInventoryPosition
import com.example.movie.domain.State
import org.apache.kafka.clients.producer.KafkaProducer
import com.example.gred.infrastructure.kafka.inventorypositionmovement.State as StateProto

internal fun State.convert(): StateProto? = when (this) {
  State.FROZEN -> StateProto.FROZEN
  State.THAWING -> StateProto.THAWING
  State.AMBIENT -> StateProto.AMBIENT
  else -> null
}

data class Producers(val movement: KafkaProducer<Metadata, MoveInventoryPosition>)
