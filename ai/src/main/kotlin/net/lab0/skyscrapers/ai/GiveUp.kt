package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook

object GiveUp: Ai {
  override val name = "GiveUp"

  override fun think(
    player: Int,
    state: GameState,
    ruleBook: RuleBook
  ) = TurnType.GiveUpTurn(player)
}
