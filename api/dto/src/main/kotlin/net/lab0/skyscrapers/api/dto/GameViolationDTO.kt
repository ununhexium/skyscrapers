package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameRuleViolation
import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl

@Serializable
data class GameViolationDTO(
  val name: String,
  val description: String,
  val detail: String,
) {
  constructor(violation: GameRuleViolation) :
      this(
        violation.name,
        violation.description,
        violation.detail,
      )

  fun toModel(): GameRuleViolation = GameRuleViolationImpl(
    name,
    description,
    detail,
  )
}
