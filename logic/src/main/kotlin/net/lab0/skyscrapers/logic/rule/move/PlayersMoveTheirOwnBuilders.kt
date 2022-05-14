package net.lab0.skyscrapers.logic.rule.move

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.MoveAndTurn
import net.lab0.skyscrapers.logic.rule.AbstractRule

object PlayersMoveTheirOwnBuilders : net.lab0.skyscrapers.logic.rule.AbstractRule<MoveAndTurn>(
  "Self Control",
  "Checks that the player moves a builder they own",
  { state: GameState, turn: MoveAndTurn ->
    if (state.builders[turn.start] != turn.player)
      "The builder at ${turn.start} doesn't belong to player#${turn.player}. It belongs to player#${state.builders[turn.start]}"
    else null
  }
)
