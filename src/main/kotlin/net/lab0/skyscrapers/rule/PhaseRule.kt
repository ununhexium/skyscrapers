package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.structure.Phase

object PhaseRule : Rule<TurnType> {
  override val name = "Check that this is the right type of turn"
  override val description =
    "Prevents movement in placement phase and prevents placement in the movement phase."

  override fun checkRule(
    state: GameState,
    turn: TurnType
  ): List<GameRuleViolation> {
    val phase = when(turn) {
      is TurnType.MoveTurn.MoveAndBuildTurn -> Phase.MOVEMENT
      is TurnType.MoveTurn.MoveAndSealTurn -> Phase.MOVEMENT
      is TurnType.PlacementTurn -> Phase.PLACEMENT
      is TurnType.GiveUpTurn -> return listOf()
    }

    if(state.phase != phase) {
      val verb = when(phase) {
        Phase.PLACEMENT -> "place"
        Phase.MOVEMENT -> "move"
        Phase.FINISHED -> "finish"
      }
      val desc = when(state.phase){
        Phase.PLACEMENT -> "placement"
        Phase.MOVEMENT -> "movement"
        Phase.FINISHED -> "finished"
      }
      return listOf(GameRuleViolationImpl(this, "Can't $verb a builder during the $desc phase"))
    }

    return listOf()
  }
}