package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Bounds
import net.lab0.skyscrapers.api.structure.Build
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.GiveUp
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Matrix
import net.lab0.skyscrapers.api.structure.Placement
import net.lab0.skyscrapers.api.structure.Player
import net.lab0.skyscrapers.api.structure.Seal
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.editor
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.rule.exception.GameRuleViolationException
import net.lab0.skyscrapers.engine.exception.InvalidBlocksConfiguration
import net.lab0.skyscrapers.engine.rule.RuleBook
import java.util.*

class GameImpl(
  override val width: Int,
  override val height: Int,
  override val playerCount: Int,
  override val maxBuildersPerPlayer: Int,
  override val ruleBook: RuleBook,
  initialBlocks: BlocksData,
) : Game {

  private val internalHistory = LinkedList<GameState>()

  override val history
    get() = internalHistory.toList()

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

    internalHistory.add(
      GameState(
        Bounds(0 until width, 0 until height),
        (0 until playerCount).map { Player(it) },
        maxBuildersPerPlayer,
        initialBlocks,
        Matrix(
          height,
          width
        ) { Height(0) },
        Matrix(height, width) { false },
        Matrix(height, width) { null },
      )
    )
  }

  private var internalTurns = 0

  override val turn: Int
    get() = internalTurns

  @kotlin.jvm.Throws(GameRuleViolationException::class)
  override fun play(turn: TurnType) {
    val violations = ruleBook.tryToPlay(turn, state)

    if (violations.isEmpty()) {
      when (turn) {
        is TurnType.PlacementTurn -> addBuilder(turn)
        is TurnType.GiveUpTurn -> giveUp(turn)
        is TurnType.MoveTurn -> {
          when (turn) {
            is TurnType.MoveTurn.BuildTurn -> moveAndBuild(turn)
            is TurnType.MoveTurn.SealTurn -> moveAndSeal(turn)
            is TurnType.MoveTurn.WinTurn -> moveAndWin(turn)
          }
        }
      }
    } else throw GameRuleViolationException(violations)

    internalTurns++
  }

  private fun moveAndWin(turn: TurnType.MoveTurn) {
    internalHistory.add(
      state.editor().move(turn).copy(
        players = state.players.map {
          if (it.id == turn.player) it else it.copy(
            active = false
          )
        }
      )
    )
  }

  override fun addBuilder(turn: Placement) {
    internalHistory.add(
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
    internalHistory.add(
      state.copy(
        // disable to current player and rotate
        players = state.players.drop(1) + state.players
          .first()
          .copy(active = false),
      )
    )
  }

  override fun moveAndBuild(
    turn: Build,
  ) {
    // remove the block that will be used to increase the height
    val nextHeight = state.buildings[turn.build] + 1

    internalHistory.add(
      state.copy(
        players = rotateToNextPlayer(state.players),

        builders = state.editor().move(turn).builders,

        blocks = state.blocks.removeBlockOfHeight(nextHeight),

        buildings = state.buildings.copyAndSet(
          turn.build,
          state.buildings[turn.build] + 1
        )
      )
    )
  }

  override fun moveAndSeal(turn: Seal) {
    internalHistory.add(
      state.copy(
        players = rotateToNextPlayer(state.players),
        builders = state.editor().move(turn).builders,
        blocks = state.blocks.removeBlockOfHeight(Height.SEAL),
        seals = state.seals.copyAndSet(turn.seal, true)
      )
    )
  }

  override fun getState(step: Int) =
    internalHistory.reversed()[step]

  override val state
    get() = internalHistory.last()

  override fun undo() {
    internalTurns--
    internalHistory.removeLast()
  }

  class Backdoor(val game: GameImpl) {
    fun forceState(state: GameState) {
      game.internalHistory.add(state)
    }
  }

  val backdoor = Backdoor(this)
}
