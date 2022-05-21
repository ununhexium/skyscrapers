package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import java.io.Writer

class GameCli : CliktCommand() {

  companion object {

    fun new(
      writer: Writer = Writer.nullWriter(),
      configurer: Configurer = Configurer(Constants.configLocation)
    ): CliktCommand {
      return GameCli().subcommands(
        Connect(),
        Configuration(writer, configurer),
//        Next(series),
//        ShowCli(series, writer),
//        PlaceBuilder().subcommands(
//          PlaceBuilderRandomly(series),
//          PlaceAt(series),
//        ),
//        MoveBuilder(series)
      )
    }
  }

  override fun run() = Unit
}
