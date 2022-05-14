package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.core.CliktCommand
import net.lab0.skyscrapers.logic.api.Game
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicReference

class ShowCli(private val ref: AtomicReference<Game?>, private val writer: PrintWriter) :
  CliktCommand(name = "show") {
  override fun run() {
    val game = ref.get()
    if (game == null) {
      writer.println("No game is instantiated")
    } else {
      writer.println(game.state.toCompositeString())
    }
  }
}
