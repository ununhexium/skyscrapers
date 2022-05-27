package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val errors: List<String>) {
  constructor(vararg errors: String) : this(errors.toList())
}