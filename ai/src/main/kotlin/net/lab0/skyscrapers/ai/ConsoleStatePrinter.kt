package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.logic.api.GameState

object ConsoleStatePrinter : (GameState) -> Unit {

  override fun invoke(state: GameState) {
    println(state.toCompositeString())
    println()
  }
}
