package com.example.infrastructure.config

class DefaultConfig(val kafkaProducerConfig: KafkaConfig, val apiCodes: Map<Int, String>)

data class KafkaConfig(val topic: String, val tenantId: String)
