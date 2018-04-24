package com.example.rest

import com.example.gred.infrastructure.rest.inventorypositionmovement.Movement
import com.example.movie.domain.Movement as DomainMovement
import com.example.movie.domain.State as DomainState

internal data class ApiError(val code: Int, val message: String)
internal data class Health(val message: String, val timestamp: String)

internal fun Movement.toDomain() = DomainMovement(
  id = null,
  locationNumber = locationNumber,
  itemNumber = itemNumber,
  fromState = toDomainState(fromState),
  toState = toDomainState(toState),
  quantity = quantity,
  override = override,
  timestamp = OffsetDateTime.parse(timestamp)
)

internal fun toDomainState(state: String?): DomainState? = when (state?.toUpperCase()) {
  DomainState.FROZEN.toString() -> DomainState.FROZEN
  DomainState.THAWING.toString() -> DomainState.THAWING
  DomainState.AMBIENT.toString() -> DomainState.AMBIENT
  null -> null
  else -> throw IllegalArgumentException("Invalid state: $state")
}
