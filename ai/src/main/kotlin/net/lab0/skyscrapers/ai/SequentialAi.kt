package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.Defaults
import net.lab0.skyscrapers.engine.rule.RuleBook
import net.lab0.skyscrapers.engine.utils.StateBrowser

class SequentialAi(override val name: String = "SequentialAi#$counter") : AbstractAi() {
  companion object {
    var counter = 0;
  }

  init {
    counter++
  }

  override fun findPlacementTurn(player: Int, state: GameState): TurnType.PlacementTurn? =
    StateBrowser(state, Defaults.RULE_BOOK).getPlaceableTurns(player).random()

  override fun findMovementTurn(
    player: Int,
    state: GameState,
    ruleBook: RuleBook
  ): TurnType? {
    return StateBrowser(state, ruleBook).getWinnableTurns(player).firstOrNull()
      ?: StateBrowser(state, ruleBook).getBuildableTurns(player).firstOrNull()
  }

}