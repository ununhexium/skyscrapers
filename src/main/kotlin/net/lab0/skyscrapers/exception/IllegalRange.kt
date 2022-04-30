package net.lab0.skyscrapers.exception

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Position

data class IllegalRange(
  override val name: String,
  override val description: String,
  val builder: Position,
  val sealOrBuild: Position,
  val reason: String
) : GameRuleViolation by GameRuleViolationImpl(
  name,
  description,
  "Can't use the builder at $builder to build at ${sealOrBuild}: $reason"
)
