package com.example.infrastructure.akka

import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.actor.SupervisorStrategyConfigurator
import scala.concurrent.duration.Duration

class ProducerSupervisorStrategy : SupervisorStrategyConfigurator {
  private val strategy = OneForOneStrategy(-1, Duration.Inf(), { SupervisorStrategy.restart() })
  override fun create() = strategy
}
