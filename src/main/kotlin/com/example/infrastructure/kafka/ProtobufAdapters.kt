package com.example.infrastructure.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

abstract class Adapter {
  open fun close() {}
  open fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
}

class MetadataAdapter(private val mapper: ObjectMapper) : Adapter(), Serializer<Metadata>, Deserializer<Metadata> {
  override fun serialize(topic: String?, data: Metadata?): ByteArray = mapper.writeValueAsBytes(data)
  override fun deserialize(topic: String?, data: ByteArray?): Metadata = mapper.readValue(data, Metadata::class.java)
}

class CreateMovieAdapter(private val mapper: ObjectMapper) : Adapter(), Serializer<CreateMovie>, Deserializer<CreateMovie> {
  override fun serialize(topic: String?, data: CreateMovie?): ByteArray = mapper.writeValueAsBytes(data)
  override fun deserialize(topic: String?, data: ByteArray?): CreateMovie = mapper.readValue(data, CreateMovie::class.java)
}
