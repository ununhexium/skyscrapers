package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.TurnType

@Serializable
data class PlaceTurnDTO(val player: Int, val position: PositionDTO) {
  fun toModel() =
    TurnType.PlacementTurn(player, position.toModel())
}
