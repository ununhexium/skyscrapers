package net.lab0.skyscrapers

import net.lab0.skyscrapers.exception.*
import net.lab0.skyscrapers.state.GameStateData
import net.lab0.skyscrapers.state.Matrix
import java.util.LinkedList

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,

  val initialBlocks: Map<Height, Int>,

  private var buildings: Matrix<Height> =
    Matrix(height, width) { Height(0) },

  private var seals: Matrix<Boolean> =
    Matrix(height, width) { false },

  private var builders: Matrix<Int?> =
    Matrix(height, width) { null },
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

  override fun getHeight(column: Int, row: Int) = buildings[column, row]

//  override fun getBuilders(player: Int) =
//    builders[player]?.toList()
//      ?: throw Exception("The player #$player doesn't exist")

  override fun getBuilders(player: Int): List<Position> {
    val result = mutableListOf<Position?>()

    builders.mapIndexedTo(result) { position, cell ->
      if (cell == player) position else null
    }

    return result.filterNotNull()
  }

  override fun hasBuilder(position: Position) =
    builders[position] != null

  override val phase: Phase
    get() {
      val placedBuilders = builders.count { it != null }
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

    builders = builders.copyAndSet(position, player)
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
    start: Position,
    target: Position,
    building: Position
  ) {
    checkMovement(player, start, target)

    val (nextHeight, nextBuildersPosition) = checkBuilding(
      building,
      target,
      start
    )

    builders = nextBuildersPosition

    // remove the block that will be used to increase the height
    currentBlocks[nextHeight] = currentBlocks[nextHeight]!! - 1

    buildings = buildings.copyAndSet(building, buildings[building] + 1)
  }

  override fun moveAndBuildSeal(
    player: Int,
    start: Position,
    target: Position,
    seal: Position
  ) {
    checkMovement(player, start, target)

    val nextBuilderPosition = builders.copyAndSwap(start, target)

    if (seals[target])
      throw IllegalMove(
        start,
        target,
        "the position [${target.x}, ${target.y}] is sealed"
      )

    builders = nextBuilderPosition
    seals = seals.copyAndSet(seal, true)
  }

  private fun checkBuilding(
    building: Position,
    to: Position,
    from: Position
  ): Pair<Height, Matrix<Int?>> {
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

    val nextBuildersPosition = builders.copyAndSwap(from, to)

    if (nextBuildersPosition[building] != null)
      throw IllegalBuilding(
        to,
        building,
        "builder is present at build location"
      )
    return Pair(nextHeight, nextBuildersPosition)
  }

  private fun checkMovement(
    player: Int,
    start: Position,
    target: Position
  ) {
    if (phase != Phase.BUILD)
      throw IllegalMove(start, target, "this is the placement phase")

    if (currentPlayer != player)
      throw WrongPlayerTurn(player, currentPlayer)

    if (outsideTheBoard(start))
      throw IllegalMove(start, target, "can't move from out of bounds")

    val originatingBuilder = builders[start]
      ?: throw IllegalMove(
        start,
        target,
        "there is no builder in the starting position"
      )

    if (originatingBuilder != player)
      throw IllegalMove(
        start,
        target,
        "can't move another player's builder"
      )

    if (outsideTheBoard(target))
      throw IllegalMove(
        start,
        target,
        "the target position can't be outside of the board"
      )

    if (hasBuilder(target))
      throw IllegalMove(
        start,
        target,
        "there is a builder at the target position"
      )

    if (start == target)
      throw IllegalMove(start, target, "the position must be different")

    if (!start.nextTo(target))
      throw IllegalMove(start, target, "too far away")

    val heightDifference = getHeight(target) - getHeight(start)

    if (heightDifference > 1)
      throw IllegalMove(
        start,
        target,
        "can't move more than 1 level each step. You tried to move up by ${heightDifference.value} levels"
      )

    if (seals[target])
      throw IllegalMove(
        start,
        target,
        "the position [${target.x}, ${target.y}] is sealed"
      )
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }

  class GameBackdoor(private val game: GameImpl) {
    fun setHeight(pos: Position, height: Int) {
      game.buildings = game.buildings.copyAndSet(pos, Height(height))
    }
  }

  val backdoor = GameBackdoor(this)

  override fun hasSeal(pos: Position): Boolean =
    seals[pos]

  override fun getState(): GameStateData {
    return GameStateData(
      buildings.map { it.value },
      seals.copy(),
      builders.copy(),
    )
  }
}

