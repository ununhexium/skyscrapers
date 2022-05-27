package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListGamesResponse(
  val names: List<String>
)