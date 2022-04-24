package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.ActionDSL
import net.lab0.skyscrapers.exception.PlayerDoesntExist

class GameImpl(
  override val width: Int = 5,
  override val height: Int = 5,
  override val playerCount: Int = 2,
  override val maxBuildersPerPlayer: Int = 2,
  private val buildings: Array<Array<Int>> = Array(width) { Array(height) { 0 } },
  private val players: Map<Int, MutableSet<Position>> = (0 until playerCount)
    .toList()
    .map { it to mutableSetOf<Position>() }
    .toMap()
    .toMutableMap(),
) : Game {

  var internalTurns = 0

  override fun get(x: Int, y: Int) = buildings[x][y]

  override fun getBuilders(player: Int) =
    players[player]?.toList()
      ?: throw Exception("The player #$player doesn't exist")

  override val phase: Phase
    get() {
      val placedBuilders = players.values.sumOf { it.size }
      return if (placedBuilders < totalBuilders) Phase.PLACE else Phase.BUILD
    }

  override fun play(action: ActionDSL.() -> (Game) -> Unit) {
    val dsl = ActionDSL()
    val change = action(dsl)
    change(this)
    internalTurns++
  }

  override fun addBuilder(player: Int, position: Position) {
    val builders = players[player] ?: throw PlayerDoesntExist(player)
    builders.add(position)
  }

  override val turn: Int
    get() = internalTurns
}
