package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.PlayerDSL
import net.lab0.skyscrapers.exception.CantGiveUpInThePlacementPhase
import net.lab0.skyscrapers.exception.CellUsedByAnotherBuilder
import net.lab0.skyscrapers.exception.PlayerDoesntExist
import java.util.LinkedList

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,
  private val buildings: Array<Array<Int>> = Array(width) { Array(height) { 0 } },
  private val builders: Map<Int, MutableSet<Position>> = (0 until playerCount)
    .toList().associateWith { mutableSetOf<Position>() }
    .toMutableMap(),
) : Game {

  companion object : NewGame

  private var internalTurns = 0

  override val turn: Int
    get() = internalTurns

  data class Player(val id: Int, var active: Boolean)

  private val playersQueue =
    (0 until playerCount).mapTo(LinkedList()) { Player(it, true) }

  override val currentPlayer: Int
    get() = playersQueue.first.id

  override fun get(x: Int, y: Int) = buildings[x][y]

  override fun getBuilders(player: Int) =
    builders[player]?.toList()
      ?: throw Exception("The player #$player doesn't exist")

  override fun hasBuilder(position: Position) =
    builders.values.any { it.contains(position) }

  override val phase: Phase
    get() {
      val placedBuilders = builders.values.sumOf { it.size }
      return if (placedBuilders < totalBuilders) Phase.PLACEMENT else Phase.BUILD
    }

  override fun play(action: PlayerDSL.() -> (Game) -> Unit) {
    val dsl = PlayerDSL()
    val change = action(dsl)
    change(this)

    internalTurns++

    // cycle players
    if (!isFinished()) {
      do {
        val current = playersQueue.removeFirst()
        playersQueue.addLast(current)
      } while (!playersQueue.first.active)
    }
  }

  override fun addBuilder(player: Int, position: Position) {
    if (hasBuilder(position))
      throw CellUsedByAnotherBuilder(position)

    val builders = builders[player] ?: throw PlayerDoesntExist(player)
    builders.add(position)
  }

  override fun giveUp(player: Int) {
    if (phase == Phase.PLACEMENT)
      throw CantGiveUpInThePlacementPhase()
    playersQueue.first.active = false
  }

  override fun moveBuilder(player: Int, from: Position, to: Position) {
    // TODO implement and test
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }
}
