package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook

class StateBrowser(val state: GameState, val ruleBook: RuleBook) {

  fun getBuilderPositionsForPlayer(player: Int): Sequence<Position> = state
    .builders
    .asSequence()
    .filter { it.second == player }
    .map { it.first }


  /**
   * Check which builders are movable.
   * Doesn't check if they can complete their action (move+build, move+seal, move+win)
   *
   * @return a list of the builders that can be moved for the given player.
   */
  fun getMovableBuilders(player: Int): Sequence<Position> =
    getBuilderPositionsForPlayer(player)
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

  /**
   * @return The builders that can move to a winning position.
   */
  fun getWinnableBuilders(player: Int): Sequence<Movement> =
    getBuilderPositionsForPlayer(player)
      .flatMap { builder ->
        val targets = builder
          .getSurroundingPositionsWithin(state.bounds)
          .filter {
            state.buildings[it] == state.blocks.maxHeight() &&
                ruleBook.canMove(MoveOnly.make(builder, it), state)
          }
        targets.map { Movement(builder, it) }
      }

  fun getTargetPositions(player: Int): Sequence<Movement> =
    getBuilderPositionsForPlayer(player)
      .flatMap { builder ->
        builder.getSurroundingPositionsWithin(state.bounds)
          .filter { ruleBook.canMove(builder, it, state) }
          .map { Movement(builder, it) }
      }

}

