package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Position

sealed class TurnType(override val player: Int) : Turn {
  data class PlacementTurn(
    override val player: Int,
    override val position: Position,
  ) : TurnType(player), Placement

  data class GiveUpTurn(override val player: Int) : TurnType(player), GiveUp

  sealed class MoveTurn(
    override val player: Int,
    override val start: Position,
    override val target: Position,
  ) : TurnType(player), Move {

    data class BuildTurn(
      override val player: Int,
      override val start: Position,
      override val target: Position,
      override val build: Position,
    ) : MoveTurn(player, start, target), Build

    data class SealTurn(
      override val player: Int,
      override val start: Position,
      override val target: Position,
      override val seal: Position,
    ) : MoveTurn(player, start, target), Seal

    data class WinTurn(
      override val player: Int,
      override val start: Position,
      override val target: Position,
    ): MoveTurn(player, start, target), Win
  }
}
