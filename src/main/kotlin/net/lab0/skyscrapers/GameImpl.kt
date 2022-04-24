package net.lab0.skyscrapers

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,
  val buildings: Array<Array<Int>> = Array(width) { Array(height) { 0 } },
  val players: Map<Int, Set<Position>> = (1..playerCount)
    .toList()
    .map { it to mutableSetOf<Position>() }
    .toMap()
    .toMutableMap(),
) : Game {
  override fun get(x: Int, y: Int) = buildings[x][y]

  override fun getBuilders(player: Int) = players[player] ?: throw Exception("the player $player doesn't exist")
}
