package net.lab0.skyscrapers.state

interface GameState {
  val buildings: Matrix<Int>
  val seals: Matrix<Boolean>
  val builders: Matrix<Int?>
}
