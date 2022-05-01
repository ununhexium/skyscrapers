package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.exception.*
import net.lab0.skyscrapers.rule.*
import net.lab0.skyscrapers.rule.move.*
import net.lab0.skyscrapers.rule.move.build.BlocksAvailabilityRule
import net.lab0.skyscrapers.rule.move.build.BuilderPreventsBuildingRule
import net.lab0.skyscrapers.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.structure.*
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

  private val turnRules: List<Rule<TurnType>> = listOf(
    PhaseRule,
    CheckCurrentPlayer,
    CantGiveUpDuringPlacementRule,
  ),

  private val placementRules: List<Rule<TurnType.PlacementTurn>> = listOf(
    PlaceBuilderOnEmptyCell,
  ),

  private val moveRules: List<Rule<TurnType.MoveTurn>> = listOf(
    BoardMoveContainmentRule,
    DefaultBuildersMovementRules,
    MovementRangeRule(),
    ClimbingRule(),
    BuildingRangeRule(),
    SealsPreventAnyMoveAndAction,
  ),

  private val buildRules: List<Rule<TurnType.MoveTurn.BuildTurn>> = listOf(
    BlocksAvailabilityRule(),
    BuilderPreventsBuildingRule,
  ),
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

  private val playersQueue =
    (0 until playerCount).mapTo(LinkedList()) { Player(it, true) }

  override val currentPlayer: Int
    get() = playersQueue.first.id

  private val currentBlocks: MutableMap<Height, Int> =
    initialBlocks.toMutableMap()

  override val blocks: Map<Height, Int>
    get() = currentBlocks.toMap()

  override fun getHeight(column: Int, row: Int) = buildings[column, row]

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
      return if (placedBuilders < totalBuilders) Phase.PLACEMENT else Phase.MOVEMENT
    }

  // TODO: add rulesbook to manage all the rules and their application
  @kotlin.jvm.Throws(GameRuleViolationException::class)
  override fun play(turn: TurnType) {
    throwIfViolatedRule(turnRules, turn, getState())

    when (turn) {
      is TurnType.PlacementTurn -> {
        throwIfViolatedRule(placementRules, turn, getState())
        addBuilder(turn)
      }
      is TurnType.GiveUpTurn -> giveUp(turn)
      is TurnType.MoveTurn -> {
        throwIfViolatedRule(moveRules, turn, getState())

        val nextState = move(turn, getState())

        when (turn) {
          is TurnType.MoveTurn.BuildTurn -> {
            throwIfViolatedRule(buildRules, turn, nextState)
            moveAndBuild(turn)
          }
          is TurnType.MoveTurn.SealTurn -> moveAndSeal(turn)
        }
      }
    }

    internalTurns++

    // cycle players
    if (!isFinished()) {
      do {
        val current = playersQueue.removeFirst()
        playersQueue.addLast(current)
      } while (!playersQueue.first.active)
    }
  }

  private fun move(turn: Move, state: GameStateData): GameState =
    getState().copy(
      builders = state.builders.copyAndSwap(turn.start, turn.target)
    )

  private inline fun <reified T> throwIfViolatedRule(
    rules: List<Rule<T>>,
    turn: T,
    state:GameState,
  ) where T : TurnType {
    val violatedRule = rules.firstOrNull {
      it.checkRule(state, turn).isNotEmpty()
    }

    if (violatedRule != null) {
      throw GameRuleViolationException(
        violatedRule.checkRule(state, turn)
      )
    }
  }

  override fun addBuilder(turn: Placement) {
    builders = builders.copyAndSet(turn.position, turn.player)
  }

  override fun giveUp(turn: GiveUp) {
    playersQueue.first.active = false
  }

  override fun moveAndBuild(
    turn: Build,
  ) {
    builders = move(turn, getState()).builders

    // remove the block that will be used to increase the height
    val nextHeight = buildings[turn.build] + 1
    currentBlocks[nextHeight] = currentBlocks[nextHeight]!! - 1

    buildings = buildings.copyAndSet(turn.build, buildings[turn.build] + 1)
  }

  override fun moveAndSeal(turn: Seal) {
    val nextBuilderPosition = builders.copyAndSwap(turn.start, turn.target)

    if (seals[turn.target])
      throw IllegalMove(
        turn.start,
        turn.target,
        "the position [${turn.target.x}, ${turn.target.y}] is sealed"
      )

    builders = nextBuilderPosition
    seals = seals.copyAndSet(turn.seal, true)
  }

  override fun isFinished(): Boolean {
    return playersQueue.count { it.active } == 1
  }

  override fun hasSeal(seal: Position) =
    seals[seal]

  override fun getState(): GameStateData {
    return GameStateData(
      phase,
      blocks,
      currentPlayer,
      buildings.map { it.value },
      seals.copy(),
      builders.copy(),
    )
  }

  // For testing only, of course :)
  inner class GameBackdoor(private val game: GameImpl) {
    fun setHeight(pos: Position, height: Int) {
      game.buildings = game.buildings.copyAndSet(pos, Height(height))
    }

    fun addSeal(p: Position) =
      addSeal(p.y, p.x)

    fun addSeal(x: Int, y: Int) {
      game.seals = game.seals.copyAndSet(y, x, true)
    }

    fun addBuilder(player: Int, position: Position) {
      game.builders = game.builders.copyAndSet(position, player)
    }
  }

  val backdoor = GameBackdoor(this)
}
