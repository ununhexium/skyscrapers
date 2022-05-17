package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook
import net.lab0.skyscrapers.engine.structure.Position

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
    ruleBook.canMove(MoveOnly.make(builder, target), state)
}