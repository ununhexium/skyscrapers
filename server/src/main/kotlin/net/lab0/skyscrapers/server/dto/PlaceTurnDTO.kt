package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceTurnDTO(val player: Int, val position: PositionDTO)