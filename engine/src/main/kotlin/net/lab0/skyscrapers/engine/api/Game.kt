package net.lab0.skyscrapers.engine.api

import net.lab0.skyscrapers.api.structure.Build
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.GiveUp
import net.lab0.skyscrapers.api.structure.Placement
import net.lab0.skyscrapers.api.structure.Seal
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.exception.GameRuleViolationException
import net.lab0.skyscrapers.engine.rule.RuleBook

/**
 * Represents a game.
 *
 * The game is played on a width * height square board.
 *
 * Placing phase.
 * At the start, each player will put in turn their builder on an empty position of the board.
 *
 * Building phase.
 * On each turn, a play must do the following actions:
 * - Move a builder to an empty position
 * - Increase the height of a tower in the 8 cells around where the builder was moved.
 *
 * If a player can't do one of the above actions, he looses.
 *
 */
interface Game {
  val height: Int
  val width: Int

  /**
   * Number of players on the board.
   */
  val playerCount: Int

  /**
   * Total number of turns that have been played
   */
  val turn: Int

  /**
   * Max number of builder per player.
   */
  val maxBuildersPerPlayer: Int

  val totalBuilders: Int
    get() = playerCount * maxBuildersPerPlayer

  val ruleBook: RuleBook

  @kotlin.jvm.Throws(GameRuleViolationException::class)
  fun play(turn: TurnType)

  fun addBuilder(turn: Placement)

  /**
   * The player abandons the game.
   * The other players continue playing until there is only 1 remaining.
   */
  fun giveUp(turn: GiveUp)

  /**
   * Moves a builder of a player from a position to another
   * and checks for the validity of that action
   */
  fun moveAndBuild(turn: Build)

  fun moveAndSeal(turn: Seal)

  /**
   * Get a specific state from the history
   */
  fun getState(step: Int): GameState

  /**
   * The current state
   */
  val state: GameState

  val history: List<GameState>

  /**
   * Undo the last turn
   */
  fun undo()
}
