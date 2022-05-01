package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.Height

object ConsoleStatePrinter : (GameState) -> Unit {

  data class MergedPosition(
    val building: Int,
    val seal: Boolean,
    val builder: Int?
  )

  override fun invoke(state: GameState) {

    val fullState =
      state.buildings.merge(state.seals.merge(state.builders) { it }) {
        MergedPosition(
          it.first.value,
          it.second.first,
          it.second.second
        )
      }

    println("Board")
    println(
      fullState.toString {
        val builder = it.builder

        if (it.seal) "  "
        else if (builder != null) ('A' + builder).toString() + it.building
        else it.building.toString().padStart(2, ' ')
      }
    )

    println()
  }
}
