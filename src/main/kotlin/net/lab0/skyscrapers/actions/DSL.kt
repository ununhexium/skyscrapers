package net.lab0.skyscrapers.actions

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

class MoveBuilderDSL() {
  lateinit var from: Position
  lateinit var to: Position

  fun from(x: Int, y: Int) {
    from = Position(x,y)
  }
  fun to(x: Int, y: Int) {
    to = Position(x,y)
  }
}

data class MoveStepFromDSL(val player: Int) {
  fun from(x:Int, y:Int) = MoveStepToDSL(player, Position(x,y))
}

data class MoveStepToDSL(val player: Int, val from: Position) {
  fun to(x:Int, y:Int) = BuildDSL(player, from, Position(x,y))
}

data class BuildingStepDSL(val player: Int) {
  fun giveUp(): Action = { game ->
    game.giveUp(player)
  }

  fun move() = MoveStepFromDSL(player)
}
