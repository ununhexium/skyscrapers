package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height

@Serializable
data class GameStateDTO(
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
  constructor(state: GameState) : this(
      bounds = BoundsDTO(state.bounds),
      players = state.players.map { PlayerDTO(it) },
      currentPlayer = state.currentPlayer,
      maxBuildersPerPlayer = state.maxBuildersPerPlayer,
      blocks = BlocksDataDTO(state.blocks),
      buildings = MatrixDTO<Int>(state.buildings.map { it.value }),
      seals = MatrixDTO<Boolean>(state.seals),
      builders = MatrixDTO<Int?>(state.builders),
      phase = PhaseDTO.from(state.phase),
  )

  fun toModel(): GameState = GameState(
    bounds = bounds.toModel(),
    players = players.map { it.toModel() },
    maxBuildersPerPlayer = maxBuildersPerPlayer,
    blocks = blocks.toModel(),
    buildings = buildings.toModel().map(::Height),
    seals = seals.toModel(),
    builders = builders.toModel(),
  )
}