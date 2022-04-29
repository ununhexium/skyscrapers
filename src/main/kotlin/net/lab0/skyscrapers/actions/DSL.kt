package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Action
import net.lab0.skyscrapers.Position

object DSL {
  fun player(player: Int) = PhaseStepDSL(player)
}

data class PhaseStepDSL(val player: Int) {
  val placement = PlacementStepDSL(player)
  val building = BuildingStepDSL(player)
}

data class PlacementStepDSL(val player: Int) {
  fun addBuilder(x: Int, y: Int) = addBuilder(Position(x, y))

  fun addBuilder(pos: Position): Action = { game ->
    game.addBuilder(player, pos)
  }
}

data class BuildingStepDSL(val player: Int) {
  fun giveUp(): Action = { game ->
    game.giveUp(player)
  }

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
  val from: Position,
  val to: Position
) {
  fun andBuild(x: Int, y: Int) =
    andBuild(Position(x, y))

  fun andBuild(pos: Position): Action = { game ->
    game.moveAndBuild(player, from, to, pos)
  }

  fun andBuildSeal(x: Int, y: Int) =
    andBuildSeal(Position(x, y))

  fun andBuildSeal(pos: Position): Action = { game ->
    game.moveAndBuildSeal(player, from, to, pos)
  }
}
