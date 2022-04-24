package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.DSL

fun main() {
  val g = GameImpl(5, 5, 2, 1)

  println(g.phase)

  g.play {
    DSL.player(1).placement.addBuilder(0, 0)
  }

//  g.play {
//    player(0) {
//      building {
//
//      }
//    }
//  }
}
