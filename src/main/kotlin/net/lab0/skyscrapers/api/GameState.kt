package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Matrix
import net.lab0.skyscrapers.structure.Phase
import net.lab0.skyscrapers.structure.Position

interface GameState {
  val phase:Phase
  val currentPlayer: Int
  val blocks: Map<Height, Int>

  val buildings: Matrix<Int>
  val seals: Matrix<Boolean>
  val builders: Matrix<Int?>

  fun isWithinBounds(pos: Position): Boolean
}
