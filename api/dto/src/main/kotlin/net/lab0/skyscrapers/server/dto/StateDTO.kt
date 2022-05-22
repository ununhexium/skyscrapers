package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.structure.Height

@Serializable
data class StateDTO(
  val bounds: BoundsDTO,
  val players: List<PlayerDTO>,
  val currentPlayer: Int,
  val maxBuildersPerPlayer: Int,
  val blocks: BlocksDataDTO,
  val buildings: MatrixDTO<Int>,
  val seals: MatrixDTO<Boolean>,
  val builders: MatrixDTO<Int?>,
  val phase: PhaseDTO,
) {
  fun toState(): GameState = GameState(
    bounds = bounds.toBounds(),
    players = players.map { it.toPlayer() },
    maxBuildersPerPlayer = maxBuildersPerPlayer,
    blocks = blocks.toBlocks(),
    buildings = buildings.toMatrix().map(::Height),
    seals = seals.toMatrix(),
    builders = builders.toMatrix(),
  )
}
