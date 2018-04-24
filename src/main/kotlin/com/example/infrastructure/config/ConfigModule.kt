package com.example.infrastructure.config

import com.google.inject.Binder
import com.google.inject.Key
import com.typesafe.config.Config
import org.jooby.Env
import org.jooby.Jooby
import org.pmw.tinylog.Logger

/**
 * Jooby module which creates application configs
 */
class ConfigModule : Jooby.Module {
  override fun configure(env: Env, conf: Config, binder: Binder) {
    Logger.info("ConfigModule started")

    val kafkaProducerConfig = KafkaConfig(
      conf.getString("gred.kafka.producer.move-inventory-position-topic"),
      conf.getString("gred.kafka.producer.tenant-id")
    )

    val apiCodes = conf
      .getConfig("gred.api-codes")
      .entrySet()
      .map { Pair(it.key.toInt(), it.value.unwrapped().toString()) }
      .toMap()

    binder.bind(Key.get(DefaultConfig::class.java)).toInstance(DefaultConfig(kafkaProducerConfig, apiCodes))
  }
}
