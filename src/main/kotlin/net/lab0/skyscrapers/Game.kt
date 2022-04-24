package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.PlayerDSL

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
   * The player whose turn it is to play.
   *
   * Turns are 0-based.
   */
  val currentPlayer: Int

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

  fun play(action: PlayerDSL.() -> (Game) -> Unit)
  fun addBuilder(player: Int, position: Position)
  fun hasBuilder(position: Position): Boolean

  /**
   * The player abandons the game.
   * The other players continue playing until there is only 1 remaining.
   */
  fun giveUp(player: Int)

  /**
   * Moves a builder of a player from a position to another
   * and checks for the validity of that action
   */
  fun moveBuilder(player: Int, from: Position, target: Position)

  val phase: Phase
  fun isFinished(): Boolean
}
