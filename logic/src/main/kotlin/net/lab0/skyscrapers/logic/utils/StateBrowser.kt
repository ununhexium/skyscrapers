package net.lab0.skyscrapers.logic.utils

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.Move
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.RuleBook
import net.lab0.skyscrapers.logic.structure.Position

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