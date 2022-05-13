package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.MoveAndTurn
import net.lab0.skyscrapers.rule.AbstractRule

object PlayersMoveTheirOwnBuilders : AbstractRule<MoveAndTurn>(
  "Self Control",
  "Checks that the player moves a builder they own",
  { state: GameState, turn: MoveAndTurn ->
    if (state.builders[turn.start] != turn.player)
      "The builder at ${turn.start} doesn't belong to player#${turn.player}. It belongs to player#${state.builders[turn.start]}"
    else null
  }
)
