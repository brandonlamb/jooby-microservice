package com.example.rest

import com.google.inject.Binder
import com.typesafe.config.Config
import org.jooby.Deferred
import org.jooby.Env
import org.jooby.Jooby
import org.jooby.Route
import org.pmw.tinylog.Logger
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class Reactor : Jooby.Module {
  private var flux: (flux: Flux<*>) -> Flux<*> = { it }
  private var mono: (mono: Mono<*>) -> Mono<*> = { it }

  fun withFlux(adapter: (flux: Flux<*>) -> Flux<*>): Reactor {
    flux = adapter
    return this
  }

  fun withMono(adapter: (mono: Mono<*>) -> Mono<*>): Reactor {
    mono = adapter
    return this
  }

  override fun configure(env: Env, conf: Config, binder: Binder) {
    env.router().map(reactor(flux, mono))
  }

  companion object {
    fun reactor(
      flux: (flux: Flux<*>) -> Flux<*> = { it },
      mono: (mono: Mono<*>) -> Mono<*> = { it }
    ): Route.Mapper<Any> = Route.Mapper.create("reactor") { value ->
      when (value) {
        /*
        is Flux<*> -> Deferred { deferred ->
          //          flux.invoke(value).doOnError { deferred.reject(it) }.subscribe { deferred.resolve(it) }
//          value.doOnError { deferred.reject(it) }.subscribe { deferred.resolve(it) }
          value
//            .subscribeOn(Schedulers.elastic())
//            .publishOn(Schedulers.elastic())
            .doOnError { deferred.reject(it) }
            .subscribe { deferred.resolve(it) }
        }
        */
        is Flux<*> -> "flux returned"

        is Mono<*> -> Deferred { deferred ->
          value
            .doOnError {
              Logger.info("ERROR")
              deferred.reject(it)
            }
            .doOnCancel {
              Logger.error("CANCELLED")
              deferred.reject(Exception("cancelled"))
            }
            .subscribe { deferred.resolve(it) }
        }

        else -> value
      }
    }
  }
}
