package com.example.infrastructure.kafka

import com.example.gred.infrastructure.kafka.inventorypositionmovement.Metadata
import com.example.gred.infrastructure.kafka.inventorypositionmovement.MoveInventoryPosition
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

abstract class Adapter {
  open fun close() {}
  open fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
}

class MetadataProtobufAdapter : Adapter(), Serializer<Metadata>, Deserializer<Metadata> {
  override fun serialize(topic: String?, data: Metadata?): ByteArray = Metadata.ADAPTER.encode(data)
  override fun deserialize(topic: String?, data: ByteArray?): Metadata = Metadata.ADAPTER.decode(data)
}

class MoveInventoryPositionProtobufAdapter : Adapter(), Serializer<MoveInventoryPosition>, Deserializer<MoveInventoryPosition> {
  override fun serialize(topic: String?, data: MoveInventoryPosition?): ByteArray = MoveInventoryPosition.ADAPTER.encode(data)
  override fun deserialize(topic: String?, data: ByteArray?): MoveInventoryPosition = MoveInventoryPosition.ADAPTER.decode(data)
}
