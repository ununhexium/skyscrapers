package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import net.lab0.skyscrapers.action.DSL
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.structure.Position
import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random

class PlaceBuilderRandomly(val ref: AtomicReference<Game?>) : CliktCommand(
  name = "randomly"
) {

  override fun run() {
    val game = ref.get()
    game?.play(
      DSL.player(game.state.currentPlayer).placement.addBuilder(
        Position(
          Random.nextInt(
            game.state.dimentions.width
          ),
          Random.nextInt(
            game.state.dimentions.height
          )
        )
      )
    )

    // TODO: else warn
  }
}