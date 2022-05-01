package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.exception.*
import net.lab0.skyscrapers.rule.*
import net.lab0.skyscrapers.rule.move.*
import net.lab0.skyscrapers.rule.move.build.*
import net.lab0.skyscrapers.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.rule.placement.PlaceBuilderOnEmptyCell
import net.lab0.skyscrapers.structure.*
import java.util.LinkedList

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,

  initialBlocks: BlocksData,

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
    DefaultBuildersMovementRule,
    MovementRangeRule(),
    ClimbingRule(),
    SealsPreventMovingRule,
  ),

  private val buildRules: List<Rule<TurnType.MoveTurn.BuildTurn>> = listOf(
    BoardBuildingContainmentRule,
    BlocksAvailabilityRule(),
    BuildingRangeRule(),
    BuildersPreventsBuildingRule,
    SealsPreventBuildingRule,
  ),

  private val sealRules: List<Rule<TurnType.MoveTurn.SealTurn>> = listOf(
    BoardSealingContainmentRule,
    SealingRangeRule(),
    SealsPreventSealingRule,
    BuildersPreventsSealingRule,
  ),

  private val winRules: List<Rule<TurnType.MoveTurn.WinTurn>> = listOf(
    WinConditionRule,
  ),
) : Game {

  companion object : NewGame

  private val history = mutableListOf<GameState>()

  init {
    if (initialBlocks.isEmpty())
      throw InvalidBlocksConfiguration(
        "There must be at least 1 block for the game to make sense"
      )

    // all the blocks from 0 (seals) to N must be present
    // therefore the *filtered* keys must be [1, 2, 3, 4, .. N]
    val filteredKeys =
      initialBlocks.getBuildingBlocks().keys.maxOf { it.value }

    val filteredInitialBlock =
      initialBlocks.getBuildingBlocks().keys.size

    if (filteredKeys != filteredInitialBlock)
      throw InvalidBlocksConfiguration(
        "There must no gap in the blocks heights"
      )

    // checks that the block amounts are decreasing as the height increases
    // seals are a separate case, not checking their amount
    val amounts = initialBlocks
      .getBuildingBlocks()
      .toSortedMap(Height::compareTo)
      .map { it.value }

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
    val missingBlocks =
      initialBlocks.getBuildingBlocks().filterValues { it <= 0 }
    if (missingBlocks.isNotEmpty()) {
      val heights =
        missingBlocks.keys.joinToString(", ") { it.value.toString() }
      throw InvalidBlocksConfiguration(
        "If a block is proposed, its initial quantity must be at least 1. No block of for the following heights: $heights"
      )
    }

    history.add(
      GameState(
        (0 until playerCount).map { Player(it) },
        maxBuildersPerPlayer,
        initialBlocks,
        Matrix(height, width) { Height(0) },
        Matrix(height, width) { false },
        Matrix(height, width) { null },
      )
    )
  }

  private var internalTurns = 0

  override val turn: Int
    get() = internalTurns

  // TODO: add rulesbook to manage all the rules and their application
  @kotlin.jvm.Throws(GameRuleViolationException::class)
  override fun play(turn: TurnType) {
    throwIfViolatedRule(turnRules, turn, state)

    when (turn) {
      is TurnType.PlacementTurn -> {
        throwIfViolatedRule(placementRules, turn, state)
        addBuilder(turn)
      }
      is TurnType.GiveUpTurn -> giveUp(turn)
      is TurnType.MoveTurn -> {
        throwIfViolatedRule(moveRules, turn, state)

        val nextBuilderState = move(turn, state)

        when (turn) {
          is TurnType.MoveTurn.BuildTurn -> {
            throwIfViolatedRule(buildRules, turn, nextBuilderState)
            moveAndBuild(turn)
          }
          is TurnType.MoveTurn.SealTurn -> {
            throwIfViolatedRule(sealRules, turn, nextBuilderState)
            moveAndSeal(turn)
          }
          is TurnType.MoveTurn.WinTurn -> {
            throwIfViolatedRule(winRules, turn, nextBuilderState)
            moveAndWin(turn)
          }
        }
      }
    }

    internalTurns++
  }

  private fun moveAndWin(turn: TurnType.MoveTurn) {
    history.add(
      move(turn, state).copy(
        players = state.players.map {
          if (it.id == turn.player) it else it.copy(
            active = false
          )
        }
      )
    )
  }

  private fun move(turn: Move, state: GameState): GameState =
    state.copy(
      builders = state.builders.copyAndSwap(turn.start, turn.target)
    )

  private inline fun <reified T> throwIfViolatedRule(
    rules: List<Rule<T>>,
    turn: T,
    state: GameState,
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
    history.add(
      state.copy(
        builders = state.builders.copyAndSet(turn.position, turn.player),
        players = rotateToNextPlayer(state.players)
      )
    )
  }

  private fun rotateToNextPlayer(players: List<Player>): List<Player> {
    val rotatedPlayers = LinkedList(players)
    // TODO: this should be read from the state
    if (!state.isFinished()) {
      do {
        val current = rotatedPlayers.removeFirst()
        rotatedPlayers.addLast(current)
      } while (!rotatedPlayers.first.active)
    }
    return rotatedPlayers
  }

  override fun giveUp(turn: GiveUp) {
    history.add(
      state.copy(
        // disable to current player and rotate
        players = state.players.drop(1) + state.players
          .first()
          .copy(active = false)
      )
    )
  }

  override fun moveAndBuild(
    turn: Build,
  ) {
    // remove the block that will be used to increase the height
    val nextHeight = state.buildings[turn.build] + 1

    history.add(
      state.copy(
        players = rotateToNextPlayer(state.players),

        builders = move(turn, state).builders,

        blocks = state.blocks.removeBlockOfHeight(nextHeight),

        buildings = state.buildings.copyAndSet(
          turn.build,
          state.buildings[turn.build] + 1
        )
      )
    )
  }

  override fun moveAndSeal(turn: Seal) {
    history.add(
      state.copy(
        players = rotateToNextPlayer(state.players),
        builders = move(turn, state).builders,
        blocks = state.blocks.removeBlockOfHeight(Height.SEAL),
        seals = state.seals.copyAndSet(turn.seal, true)
      )
    )
  }

  override fun getState(step: Int) =
    history.reversed()[step]

  override val state
    get() = history.last()

  class Backdoor(val game: GameImpl) {
    fun forceState(state: GameState) {
      game.history.add(state)
    }
  }

  val backdoor = Backdoor(this)
}
