package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.Action
import net.lab0.skyscrapers.exception.*
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

  override fun play(action: Action) {
    action(this)

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

    if(currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    val builders = builders[player] ?: throw PlayerDoesntExist(player)
    builders.add(position)
  }

  override fun giveUp(player: Int) {
    if(currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    if (phase == Phase.PLACEMENT)
      throw CantGiveUpInThePlacementPhase()
    playersQueue.first.active = false
  }

  override fun moveBuilder(player: Int, from: Position, to: Position) {
    if(phase != Phase.BUILD)
      throw IllegalMove(from, to, "this is the placement phase")

    if(currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    val positions = builders[player]!!

    if (!positions.contains(from))
      throw IllegalMove(from, to, "there is no builder in the starting position")

    if(hasBuilder(to))
      throw IllegalMove(from, to, "there is a builder at the target position")

    if(from == to)
      throw IllegalMove(from, to, "the position must be different")

    if(!from.nextTo(to))
        throw IllegalMove(from, to, "too far away")

    if(to.x < 0 || to.x >= width || to.y < 0 || to.y >= height)
      throw IllegalMove(from, to, "the target position can't be outside of the board")

    positions.remove(from)
    positions.add(to)
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }
}
