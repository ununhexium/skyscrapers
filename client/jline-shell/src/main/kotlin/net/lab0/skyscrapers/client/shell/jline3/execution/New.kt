package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import net.lab0.skyscrapers.logic.Defaults
import net.lab0.skyscrapers.logic.api.Game
import java.util.concurrent.atomic.AtomicReference

class New(private val ref: AtomicReference<Game?>) : CliktCommand(name = "new") {
  private val width by option("-w", "--width", help = "Width of the board")
    .int()
    .default(Defaults.WIDTH)

  private val height by option("-h", "--height", help = "Height of the board")
    .int()
    .default(Defaults.HEIGHT)

  private val players by option(help = "Players")
    .int()
    .default(Defaults.PLAYER_COUNT)

  private val builders by option(help = "Builders per player")
    .int()
    .default(Defaults.BUILDERS_PER_PLAYER)

  override fun run() {
    ref.set(
      Game.new(
        width = width,
        height = height,
        playerCount = players,
        buildersPerPlayer = builders,
      )
    )
  }
}
