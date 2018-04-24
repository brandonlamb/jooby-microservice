package com.example.infrastructure.kafka

import org.apache.kafka.clients.producer.KafkaProducer

enum class Rating { G, PG, PG13, R }

data class Metadata(val correlationId: String, val timestamp: String)
data class CreateMovie(val metadata: Metadata, val data: Movie)
data class Movie(val name: String, val rating: Rating, val releaseDate: String)
data class Producers(val movement: KafkaProducer<Metadata, CreateMovie>)
