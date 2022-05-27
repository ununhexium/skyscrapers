package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.Phase

object ConsolePhaseObserver : (Phase) -> Unit {
  override fun invoke(phase: Phase) {
    println("==========")
    println(phase.name)
    println("==========")
  }
}
