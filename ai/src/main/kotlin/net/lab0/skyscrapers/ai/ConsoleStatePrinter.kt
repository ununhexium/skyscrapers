package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.CompositePosition

object ConsoleStatePrinter : (GameState) -> Unit {

  override fun invoke(state: GameState) {
    println(state.toCompositeString())
    println()
  }
}
