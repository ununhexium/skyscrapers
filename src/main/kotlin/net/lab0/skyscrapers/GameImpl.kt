package net.lab0.skyscrapers

import net.lab0.skyscrapers.exception.*
import java.util.LinkedList

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,

  val initialBlocks: Map<Height, Int>,

  private val buildings: Array<Array<Height>> =
    Array(width) { Array(height) { Height(0) } },
  private val roofs: Array<Array<Boolean>> =
    Array(width) { Array(height) { false } },
  private val builders: MutableMap<Int, Set<Position>> = (0 until playerCount)
    .toList().associateWith { mutableSetOf<Position>() }
    .toMutableMap(),
) : Game {

  companion object : NewGame

  init {
    if (initialBlocks.keys.isEmpty() || initialBlocks.entries.sumOf { it.value } == 0)
      throw InvalidBlocksConfiguration(
        "There must be at least 1 block for the game to make sense"
      )

    // all the blocks from 1 to N must be present
    // therefore the keys must be [1, 2, 3, 4, .. N]
    if (initialBlocks.keys.maxOf { it.value } != initialBlocks.keys.size)
      throw InvalidBlocksConfiguration(
        "There must no gap in the blocks heights"
      )

    // checks that the block amounts are decreasing as the height increases
    val amounts = initialBlocks.toSortedMap(Height::compareTo).map { it.value }
    val sortedAmounts = amounts.sortedDescending()
    if (amounts != sortedAmounts)
      throw InvalidBlocksConfiguration(
        "The lower blocks must be in larger quantity than the higher blocks"
      )

    /*
     * If a block is proposed, it must be present at least once,
     * otherwise it's not possible to reach the highest level to
     * finish the game.
     *
     * Given the previous tests, checking for an amount of 0 at the top
     * position is enough, but still checking everything at it's cheap.
     */
    val missingBlocks = initialBlocks.filter { it.value == 0 }
    if (missingBlocks.isNotEmpty()) {
      val heights =
        missingBlocks.keys.joinToString(", ") { it.value.toString() }
      throw InvalidBlocksConfiguration(
        "If a block is proposed, its initial quantity must be at least 1. No block of for the following heights: $heights"
      )
    }
  }

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

    builders[player] = builders[player]!! + position
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

    // POSITIONING

    checkMovement(from, to, player)

    // BUILDING

    if (outsideTheBoard(building))
      throw IllegalBuilding(to, building, "outside of the board")

    val nextHeight = buildings[building] + 1
    val availableBlocks = blocks[nextHeight]!!
    if (availableBlocks == 0)
      throw IllegalBuilding(
        to,
        building,
        "no block of height ${nextHeight.value} remaining"
      )

    val nextBuildersPosition = builders[player]!!.toMutableSet() // copy
    nextBuildersPosition.remove(from)
    nextBuildersPosition.add(to)

    val allNextBuildersPositions: Collection<Position> =
      builders.filterKeys { it != player }.values.flatten() + nextBuildersPosition

    if (allNextBuildersPositions.contains(building))
      throw IllegalBuilding(
        to,
        building,
        "builder is present at build location"
      )

    builders[player] = nextBuildersPosition

    // remove the block that will be used to increase the height
    currentBlocks[nextHeight] = availableBlocks - 1

    buildings[building.x][building.y] = buildings[building.x][building.y]++
  }

  private fun checkMovement(
    from: Position,
    to: Position,
    player: Int
  ) {
    if (phase != Phase.BUILD)
      throw IllegalMove(from, to, "this is the placement phase")

    if (currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    if (!builders[player]!!.contains(from))
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

    val heightDifference = this[to] - this[from]

    if (heightDifference > 1)
      throw IllegalMove(
        from,
        to,
        "can't move more than 1 level each step. You tried to move up by ${heightDifference.value} levels"
      )
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }

  class GameBackdoor(private val game: GameImpl) {
    fun setHeight(pos: Position, height: Int) {
      game.buildings[pos] = height
    }
  }

  val backdoor = GameBackdoor(this)

  override fun moveAndBuildRoof(
    player: Int,
    from: Position,
    to: Position,
    roof: Position
  ) {

  }
}

