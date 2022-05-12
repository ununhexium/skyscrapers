package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.groups.single
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.structure.Position
import java.util.concurrent.atomic.AtomicReference

class MoveBuilder(private val ref: AtomicReference<Game?>) : CliktCommand(name = "move-builder") {
  private val from by option("-f", "--from")
    .convert { posStr ->
      posStr.split(",")
        .map { it.toInt() }
        .let { Position(it[0], it[1]) }
    }
    .required()

  private val to by option("-t", "--to").position().required()

  private val andBuild by option("--andBuild").position()
  private val andSeal by option("--andSeal").position()

  override fun run() {
    val game = ref.get() ?: error("The game doesn't exist")

    val build = andBuild
    val seal = andSeal
    val turn = when {
      build != null -> TurnType.MoveTurn.BuildTurn(
        game.state.currentPlayer,
        from,
        to,
        build
      )
      seal != null -> TurnType.MoveTurn.SealTurn(
        game.state.currentPlayer,
        from,
        to,
        seal
      )
      else -> error("Must either build or seal")
    }

    game.play(turn)
  }
}