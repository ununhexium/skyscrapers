package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.Move
import net.lab0.skyscrapers.engine.rule.AbstractRule

object PlayersMoveTheirOwnBuilders : AbstractRule<Move>(
  "Self Control",
  "Checks that the player moves a builder they own",
  { state: GameState, turn: Move ->
    if (state.builders[turn.start] != turn.player)
      "The builder at ${turn.start} doesn't belong to player#${turn.player}. It belongs to player#${state.builders[turn.start]}"
    else null
  }
)
