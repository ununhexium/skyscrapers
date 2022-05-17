package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import net.lab0.skyscrapers.logic.action.DSL
import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.structure.Position
import java.util.concurrent.atomic.AtomicReference

class PlaceAt(val ref: AtomicReference<Game?>) : CliktCommand(name = "at") {
  private val x by option("-x", "--x-position").int().required()
  private val y by option("-y", "--y-position").int().required()

  override fun run() {
    val game = ref.get()
    game?.play(
      DSL.player(game.state.currentPlayer).placement.addBuilder(Position(x, y))
    )

    // TODO: else warn
  }
}
