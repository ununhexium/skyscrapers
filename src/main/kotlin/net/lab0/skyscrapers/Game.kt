package net.lab0.skyscrapers

/**
 * Represents a game.
 *
 * The game is played on a width * height square board.
 *
 * Game phase 1.
 * At the start, each player will put in turn their builder on an empty cell of the board.
 *
 * Game phase 2.
 * On each turn, a play must do the following actions:
 * - Move a builder to an empty cell
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
   * Max number of builder per player.
   */
  val maxBuildersPerPlayer: Int

  operator fun get(x: Int, y: Int): Int
  operator fun get(pos: Position): Int =
    this[pos.x, pos.y]

  fun getBuilders(player: Int): Collection<Position>

  fun play()
}
