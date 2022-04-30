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
    override val sealOrBuild: Position,
  ) : TurnType(player), Move {

    data class MoveAndBuildTurn(
      override val player: Int,
      override val start: Position,
      override val target: Position,
      override val build: Position,
    ) : MoveTurn(player, start, target, build), MoveAndBuild

    data class SealMoveTurn(
      override val player: Int,
      override val start: Position,
      override val target: Position,
      override val seal: Position,
    ) : MoveTurn(player, start, target, seal), MoveAndSeal
  }
}
