package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.engine.api.Game

interface Round {
  /**
   * Works on the game if it's been started
   */
  fun withGame(f: Game.() -> Unit)

  /**
   * Starts a new round with a new game.
   */
  fun start()

  /**
   * Get a specific round
   */
  fun getRound(index: Int): Game?

  /**
   * The number of rounds that have been played.
   */
  val rounds: Int
}
