package net.lab0.skyscrapers.utils

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Move
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.RuleBook
import net.lab0.skyscrapers.structure.Position

class StateBrowser(val state: GameState, val ruleBook: RuleBook) {
  fun getMovableBuilders(player: Int): List<Position> =
    state
      .builders
      .filter { it == player }
      .keys
      .filter { pos ->
        pos.getSurroundingPositionsWithin(state.bounds).any {
          ruleBook.canMove(
            TurnType.MoveTurn.WinTurn(player, pos, it),
            state
          )
        }
      }

  fun builderCanMoveTo(builder: Position, target: Position) =
    ruleBook.canMove(Move.make(builder, target), state)
}
