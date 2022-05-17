package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.structure.Phase

@Serializable
enum class PhaseDTO {
  PLACEMENT,
  MOVEMENT,
  FINISHED, ;

  companion object {
    fun from(phase: Phase) =
      when (phase) {
        Phase.PLACEMENT -> PLACEMENT
        Phase.MOVEMENT -> MOVEMENT
        Phase.FINISHED -> FINISHED
      }
  }
}
