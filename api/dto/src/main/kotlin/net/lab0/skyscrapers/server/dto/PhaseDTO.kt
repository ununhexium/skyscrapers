package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
enum class PhaseDTO {
  PLACEMENT,
  MOVEMENT,
  FINISHED, ;

  companion object {
    fun from(phase: net.lab0.skyscrapers.engine.structure.Phase) =
      when (phase) {
        net.lab0.skyscrapers.engine.structure.Phase.PLACEMENT -> PLACEMENT
        net.lab0.skyscrapers.engine.structure.Phase.MOVEMENT -> MOVEMENT
        net.lab0.skyscrapers.engine.structure.Phase.FINISHED -> FINISHED
      }
  }
}