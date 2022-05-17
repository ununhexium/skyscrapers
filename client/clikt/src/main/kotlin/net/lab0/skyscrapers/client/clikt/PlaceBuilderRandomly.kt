package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import net.lab0.skyscrapers.logic.action.DSL
import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.structure.Position
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
            game.state.bounds.width
          ),
          Random.nextInt(
            game.state.bounds.height
          )
        )
      )
    )

    // TODO: else warn
  }
}