package net.lab0.skyscrapers.engine.api

import kotlin.jvm.Throws

/**
 * A group of games between the same players.
 */
interface Series {

  companion object : NewSeries

  /**
   * Works on the game if it's been started.
   *
   * Doesn't do anything if no game has started
   *
   * @return `true` if the function was applied to the game, `false` otherwise.
   */
  fun withGame(f: Game.() -> Unit): Boolean

  /**
   * Starts a new round with a new game.
   *
   * @throws IllegalStateException if trying to start a new round once
   * the series has a winner
   */
  fun start()

  /**
   * Get a specific round.
   */
  fun getRound(index: Int): Game?

  /**
   * The number of rounds that have been played.
   */
  val rounds: Int

  /**
   * 0-based round index.
   * `null` if there is no active round.
   */
  val currentRound: Int?

  /**
   * How many rounds are necessary to win this BestOf series.
   */
  val requiredWinningRounds: Int

  /**
   * @return true if 1 player won the number of required games to win a series.
   */
  fun isFinished(): Boolean

  /**
   * @return the player who won the series.
   */
  val winner: Int?
}
