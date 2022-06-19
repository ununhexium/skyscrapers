package net.lab0.skyscrapers.ai

import mu.KotlinLogging
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.TurnType.PlacementTurn
import net.lab0.skyscrapers.engine.rule.RuleBook
import net.lab0.skyscrapers.engine.utils.StateBrowser

class RandomAi(
  override val name: String = "RandomAi#$counter"
) : AbstractAi() {
  companion object {
    var counter = 0;
  }

  init {
    counter++
  }

  override fun findPlacementTurn(player: Int, state: GameState): PlacementTurn {
    val randomPosition = state
      .bounds
      .positionsSequence
      .filterNot { state.hasBuilder(it) }
      .shuffled()
      .first()

    return PlacementTurn(player, randomPosition)
  }

  override fun findMovementTurn(player:Int, state: GameState, ruleBook: RuleBook): TurnType {
    val browser = StateBrowser(state, ruleBook)

    val choices = browser.getBuildableTurns(player)
    log.info { "Found ${choices.toList().size} possible moves." }

    val random = choices.random()
    log.info { "Chose $random" }

    return random ?: TurnType.GiveUpTurn(player)
  }
}

