package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Position

@Serializable
data class SealTurnDTO(
  val player: AccessToken,
  val start: PositionDTO,
  val target: PositionDTO,
  val seal: PositionDTO,
) {
  constructor(
    player: AccessToken,
    start: Position,
    target: Position,
    seal: Position,
  ) : this(
    player,
    PositionDTO(start),
    PositionDTO(target),
    PositionDTO(seal),
  )
}
