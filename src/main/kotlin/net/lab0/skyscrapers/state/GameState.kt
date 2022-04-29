package net.lab0.skyscrapers.state

interface GameState {
  val buildings: List<List<Int>>
  val seals: List<List<Boolean>>
  val builders: List<List<Int?>>
}
