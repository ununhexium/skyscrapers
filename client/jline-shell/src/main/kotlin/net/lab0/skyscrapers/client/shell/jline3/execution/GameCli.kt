package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.api.Game
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicReference

class GameCli : CliktCommand() {

  companion object {
    fun new(ref: AtomicReference<Game?>, writer: PrintWriter): CliktCommand =
      GameCli().subcommands(
        New(ref),
        ShowCli(ref, writer),
        PlaceBuilder().subcommands(
          PlaceBuilderRandomly(ref),
          PlaceAt(ref),
        )
      )
  }

  override fun run() = Unit
}
