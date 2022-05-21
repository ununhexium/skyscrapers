package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.structure.Height
import net.lab0.skyscrapers.engine.structure.Position

class StateEditor(val state: GameState) {
  fun height(pos: Position, height: Int) =
    state.copy(buildings = state.buildings.copyAndSet(pos, Height(height)))

  fun seal(pos: Position) =
    state.copy(seals = state.seals.copyAndSet(pos, true))

  fun placeBuilder(player: Int, position: Position) =
    state.copy(builders = state.builders.copyAndSet(position, player))

  fun move(turn: MoveOnly) =
    state.copy(builders = state.builders.copyAndSwap(turn.start, turn.target))
}