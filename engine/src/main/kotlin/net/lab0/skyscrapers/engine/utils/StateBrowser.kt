package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook

class StateBrowser(val state: GameState, val ruleBook: RuleBook) {

  fun getBuilderPositionsForPlayer(player: Int): Sequence<Position> = state
    .builders
    .filter { it == player }
    .keys
    .asSequence()


  /**
   * Check which builders are movable.
   * Doesn't check if they can complete their action (move+build, move+seal, move+win)
   *
   * @return a list of the builders that can be moved for the given player.
   */
  fun getMovableBuilders(player: Int): Sequence<Position> =
    getBuilderPositionsForPlayer(player)
      .asSequence()
      .filter { pos ->
        pos.getSurroundingPositionsWithin(state.bounds).any {
          ruleBook.canMove(
            TurnType.MoveTurn.WinTurn(player, pos, it),
            state
          )
        }
      }

  fun builderCanMoveTo(builder: Position, target: Position) =
    ruleBook.canMove(MoveOnly.make(builder, target), state)
}