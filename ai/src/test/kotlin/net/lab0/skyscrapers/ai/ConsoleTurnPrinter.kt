package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.TurnType

object ConsoleTurnPrinter : (TurnType) -> Unit {
  override fun invoke(turn: TurnType) {
    print("Player#${turn.player}")

    when (turn) {
      is TurnType.GiveUpTurn ->
        print(" gave up")

      is TurnType.PlacementTurn ->
        print(" placed ${turn.position}")

      is TurnType.MoveTurn -> {
        print(" moved from ${turn.start} to ${turn.target} and ")

        when (turn) {
          is TurnType.MoveTurn.BuildTurn -> print("built at ${turn.build}")
          is TurnType.MoveTurn.SealTurn -> print("sealed at ${turn.seal}")
          is TurnType.MoveTurn.WinTurn -> print("won")
        }
      }
    }

    println()
    println()
  }
}
