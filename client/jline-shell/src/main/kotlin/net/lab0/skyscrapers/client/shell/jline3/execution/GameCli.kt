package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.api.Game
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicReference

class GameCli : CliktCommand() {

  companion object {
    fun new(ref: AtomicReference<Game?>, writer: PrintWriter): CliktCommand =
      GameCli().subcommands(NewCli(ref), ShowCli(ref, writer))
  }

  override fun run() = Unit
}

