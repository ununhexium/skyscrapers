package net.lab0.skyscrapers.action

import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.structure.Position

object DSL {
  fun player(player: Int) = PhaseStepDSL(player)
}

data class PhaseStepDSL(val player: Int) {
  val placement = PlacementStepDSL(player)
  val building = BuildingStepDSL(player)
}

data class PlacementStepDSL(val player: Int) {
  fun addBuilder(x: Int, y: Int) = addBuilder(Position(x, y))

  fun addBuilder(pos: Position) =
    TurnType.PlacementTurn(player, pos)
}

data class BuildingStepDSL(val player: Int) {
  fun giveUp() =
    TurnType.GiveUpTurn(player)

  fun move() = MoveStepFromDSL(player)
}

data class MoveStepFromDSL(val player: Int) {
  fun from(x: Int, y: Int) = from(Position(x, y))
  fun from(pos: Position) = MoveStepToDSL(player, pos)
}

data class MoveStepToDSL(val player: Int, val from: Position) {
  fun to(x: Int, y: Int) = to(Position(x, y))
  fun to(pos: Position) = BuildDSL(player, from, pos)
}

data class BuildDSL(
  val player: Int,
  val start: Position,
  val target: Position
) {
  fun andBuild(x: Int, y: Int) =
    andBuild(Position(x, y))

  fun andBuild(pos: Position) =
    TurnType.MoveTurn.BuildTurn(player, start, target, pos)

  fun andSeal(x: Int, y: Int) =
    andSeal(Position(x, y))

  fun andSeal(pos: Position) =
    TurnType.MoveTurn.SealTurn(player, start, target, pos)

  fun andWin() =
    TurnType.MoveTurn.WinTurn(player, start, target)
}
