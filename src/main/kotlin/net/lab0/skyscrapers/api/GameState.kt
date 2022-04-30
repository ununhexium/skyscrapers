package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.Matrix

interface GameState {
  val buildings: Matrix<Int>
  val seals: Matrix<Boolean>
  val builders: Matrix<Int?>
}
