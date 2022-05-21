package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameError(val errors: List<String>) {
  constructor(vararg errors: String) : this(errors.toList())
}