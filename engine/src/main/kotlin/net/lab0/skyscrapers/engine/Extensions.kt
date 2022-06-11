package net.lab0.skyscrapers.engine

import net.lab0.skyscrapers.api.structure.GameState

fun GameState.editor() =
  StateEditor(this)

