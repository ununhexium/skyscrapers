package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceTurnDTO(val player: AccessToken, val position: PositionDTO)
