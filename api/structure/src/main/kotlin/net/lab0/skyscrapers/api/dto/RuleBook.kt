package net.lab0.skyscrapers.api.dto

import net.lab0.skyscrapers.api.structure.GameRuleViolation
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.TurnType

interface RuleBook {
  fun tryToPlay(turn: TurnType, state: GameState): List<GameRuleViolation>
  fun canMove(turn: MoveOnly, state: GameState): Boolean
}