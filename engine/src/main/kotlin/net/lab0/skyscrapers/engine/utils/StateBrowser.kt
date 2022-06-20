package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.BuildTurn
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.SealTurn
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.WinTurn
import net.lab0.skyscrapers.api.structure.TurnType.PlacementTurn
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
            WinTurn(player, pos, it),
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
        builder
          .getSurroundingPositionsWithin(state.bounds)
          .map { target -> Movement(builder, target) }
          .filter {
            state.buildings[it.target] == state.blocks.maxHeight() &&
                ruleBook.canMove(it, state)
          }
      }

  fun getTargetPositions(player: Int): Sequence<Movement> =
    getBuilderPositionsForPlayer(player)
      .flatMap { builder ->
        builder.getSurroundingPositionsWithin(state.bounds)
          .map { Movement(builder, it) }
          .filter { ruleBook.canMove(it, state) }
      }

  fun getBuildableTurns(player: Int): Sequence<BuildTurn> =
    getTargetPositions(player)
      .flatMap { mov ->
        mov.target.getSurroundingPositionsWithin(state.bounds)
          .map { BuildTurn(player, mov.start, mov.target, it) }
          .filter { ruleBook.canBuild(it, state) }
      }


  /**
   * @return The builders that can move to a winning position.
   */
  fun getWinnableTurns(player: Int): Sequence<WinTurn> =
    getBuilderPositionsForPlayer(player)
      .flatMap { builder ->
        builder
          .getSurroundingPositionsWithin(state.bounds)
          .map { target -> WinTurn(player, builder, target) }
          .filter {
            state.buildings[it.target] == state.blocks.maxHeight() &&
                ruleBook.canMove(it, state)
          }
      }

  fun getPlaceableTurns(player: Int): Sequence<PlacementTurn> =
    state.bounds.positionsSequence
      .filter { !state.hasBuilder(it) }
      .map{ PlacementTurn(player, it) }

  fun getSealableTurns(player: Int): Sequence<SealTurn> =
    getTargetPositions(player)
      .flatMap { mov ->
        mov.target.getSurroundingPositionsWithin(state.bounds)
          .map { SealTurn(player, mov.start, mov.target, it) }
          .filter { ruleBook.canSeal(it, state) }
      }
}
