package net.lab0.skyscrapers

import net.lab0.skyscrapers.exception.*
import java.util.LinkedList

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,

  val initialBlocks: Map<Height, Int>,

  private val buildings: Array<Array<Height>> = Array(width) {
    Array(height) {
      Height(
        0
      )
    }
  },
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

  private val currentBlocks: MutableMap<Height, Int> =
    initialBlocks.toMutableMap()

  override val blocks: Map<Height, Int>
    get() = currentBlocks

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

    if (currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    val builders = builders[player] ?: throw PlayerDoesntExist(player)
    builders.add(position)
  }

  override fun giveUp(player: Int) {
    if (currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    if (phase == Phase.PLACEMENT)
      throw CantGiveUpInThePlacementPhase()
    playersQueue.first.active = false
  }

  /**
   * @return true if the position is inside the board
   */
  private fun outsideTheBoard(pos: Position) =
    pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height

  override fun moveAndBuild(
    player: Int,
    from: Position,
    to: Position,
    building: Position
  ) {
    if (phase != Phase.BUILD)
      throw IllegalMove(from, to, "this is the placement phase")

    if (currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    val positions = builders[player]!!

    if (!positions.contains(from))
      throw IllegalMove(
        from,
        to,
        "there is no builder in the starting position"
      )

    if (hasBuilder(to))
      throw IllegalMove(from, to, "there is a builder at the target position")

    if (from == to)
      throw IllegalMove(from, to, "the position must be different")

    if (!from.nextTo(to))
      throw IllegalMove(from, to, "too far away")

    if (outsideTheBoard(to))
      throw IllegalMove(
        from,
        to,
        "the target position can't be outside of the board"
      )

    positions.remove(from)
    positions.add(to)

    if (outsideTheBoard(building))
      throw IllegalBuilding(to, building, "outside of the board")

    if (hasBuilder(building))
      throw IllegalBuilding(
        to,
        building,
        "builder is present at build location"
      )

    val nextHeight = buildings[building] + 1
    val availableBlocks = blocks[nextHeight]!!
    if (availableBlocks == 0)
      throw IllegalBuilding(
        to,
        building,
        "no block of height ${nextHeight.value} remaining"
      )

    // remove the block that will be used to increase the height
    currentBlocks[nextHeight] = availableBlocks - 1

    buildings[building.x][building.y] = buildings[building.x][building.y]++
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }
}

