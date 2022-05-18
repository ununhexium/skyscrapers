package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.engine.api.Series
import java.io.PrintWriter

class GameCli : CliktCommand() {

  companion object {
    fun new(series: Series, writer: PrintWriter): CliktCommand =
      GameCli().subcommands(
        Next(series),
        ShowCli(series, writer),
        PlaceBuilder().subcommands(
          PlaceBuilderRandomly(series),
          PlaceAt(series),
        ),
        MoveBuilder(series)
      )
  }

  override fun run() = Unit
}
