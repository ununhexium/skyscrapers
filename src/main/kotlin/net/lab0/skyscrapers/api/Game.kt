package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.NewGame
import net.lab0.skyscrapers.structure.GameStateData
import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Phase
import net.lab0.skyscrapers.structure.Position

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

  /**
   * The number of blocks available for each height
   */
  val blocks: Map<Height, Int>

  fun getHeight(column: Int, row: Int): Height

  fun getHeight(pos: Position): Height =
    this.getHeight(pos.y, pos.x)

  fun getBuilders(player: Int): List<Position>

  fun play(turn: TurnType)
  fun addBuilder(turn: Placement)
  fun hasBuilder(position: Position): Boolean

  /**
   * The player abandons the game.
   * The other players continue playing until there is only 1 remaining.
   */
  fun giveUp(turn: GiveUp)

  /**
   * Moves a builder of a player from a position to another
   * and checks for the validity of that action
   */
  fun moveAndBuild(turn: MoveAndBuild)

  fun moveAndSeal(turn: MoveAndSeal)

  fun hasSeal(seal: Position): Boolean

  val phase: Phase

  fun isFinished(): Boolean

  fun getState(): GameStateData
}
