package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import net.lab0.skyscrapers.engine.api.Series
import java.io.PrintWriter

class ShowCli(private val series: Series, private val writer: PrintWriter) :
  CliktCommand(name = "show") {
  override fun run() {
    series.getCurrentRound()?.let {
      writer.println(it.state.toCompositeString())
    } ?: writer.println("No game is instantiated")
  }
}
