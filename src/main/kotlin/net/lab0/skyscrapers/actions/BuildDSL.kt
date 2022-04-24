package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

data class BuildDSL(
  val player: Int,
  val from: Position,
  val to: Position
) {
  fun andBuild(x: Int, y: Int): Action = { game ->
    game.moveBuilder(player, from, to)
//    TODO game.build()
  }

//  TODO fun andSeal()
}
