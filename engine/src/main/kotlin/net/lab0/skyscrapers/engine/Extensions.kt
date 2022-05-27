package net.lab0.skyscrapers

import net.lab0.skyscrapers.engine.StateEditor
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Position

operator fun Array<Array<Height>>.get(pos: Position): Height =
  this[pos.x][pos.y]

operator fun Array<Array<Height>>.set(pos: Position, value: Int) {
  this[pos.x][pos.y] = Height(value)
}

fun GameState.editor() =
  StateEditor(this)

