package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.ActionDSL

/**
 * Represents a game.
 *
 * The game is played on a width * height square board.
 *
 * Placing phase.
 * At the start, each player will put in turn their builder on an empty cell of the board.
 *
 * Building phase.
 * On each turn, a play must do the following actions:
 * - Move a builder to an empty cell
 * - Increase the height of a tower in the 8 cells around where the builder was moved.
 *
 * If a player can't do one of the above actions, he looses.
 *
 */
interface Game {
  companion object : NewGame

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
   * The player whose turn it is to play
   *
   * For a 3 players game:
   *
   * Turn 0: player 0
   * Turn 1: player 1
   * Turn 2: player 2
   * Turn 3: player 0
   * Turn 4: player 1
   * Turn 5: player 2
   */
  val currentPlayer: Int
    get() = (turn % playerCount)

  /**
   * Max number of builder per player.
   */
  val maxBuildersPerPlayer: Int

  val totalBuilders: Int
    get() = playerCount * maxBuildersPerPlayer

  operator fun get(x: Int, y: Int): Int
  operator fun get(pos: Position): Int =
    this[pos.x, pos.y]

  fun getBuilders(player: Int): List<Position>

  fun play(action: ActionDSL.() -> (Game) -> Unit)
  fun addBuilder(player: Int, position: Position)

  val phase: Phase
}
