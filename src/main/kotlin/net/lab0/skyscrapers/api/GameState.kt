package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Matrix
import net.lab0.skyscrapers.structure.Position

interface GameState {
  val buildings: Matrix<Int>
  val seals: Matrix<Boolean>
  val builders: Matrix<Int?>

  fun isWithinBounds(pos: Position): Boolean
}
