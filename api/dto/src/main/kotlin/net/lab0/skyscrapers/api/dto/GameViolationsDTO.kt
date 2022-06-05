package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameRuleViolation
import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl

@Serializable
data class GameViolationsDTO(val violations: List<GameViolationDTO>)
