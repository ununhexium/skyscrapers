package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.Phase

object PhaseRule : AbstractRule<TurnType>(
  "Check that this is the right type of turn",
  "Prevents movement in placement phase and prevents placement in the movement phase.",
  { state: GameState, turn: TurnType ->

    fun toVerb(phase: Phase) = when (phase) {
      Phase.PLACEMENT -> "place"
      Phase.MOVEMENT -> "move"
      Phase.FINISHED -> "finish"
    }

    fun toNoun(phase: Phase) = when (phase) {
      Phase.PLACEMENT -> "placement"
      Phase.MOVEMENT -> "movement"
      Phase.FINISHED -> "finished"
    }

    val phase = when (turn) {
      is TurnType.MoveTurn -> Phase.MOVEMENT
      is TurnType.PlacementTurn -> Phase.PLACEMENT
      is TurnType.GiveUpTurn -> Phase.MOVEMENT
    }

    if (turn is TurnType.GiveUpTurn) {
      null
    } else if (state.phase != phase) {
      "Can't ${toVerb(phase)} a builder during the ${toNoun(state.phase)} phase"
    } else null
  }
)