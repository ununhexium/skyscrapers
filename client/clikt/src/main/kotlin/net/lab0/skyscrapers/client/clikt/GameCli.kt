package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.client.clikt.command.Configuration
import net.lab0.skyscrapers.client.clikt.command.Connect
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.client.OkHttp
import java.io.Writer

class GameCli : CliktCommand() {

  companion object {

    fun new(
      writer: Writer?,
      configurer: Configurer = Configurer(Constants.configLocation),
      skyscraperClient: SkyscraperClient = SkyscraperClientImpl(OkHttp()),
    ): CliktCommand {
      return GameCli().subcommands(
        Connect(writer, configurer, skyscraperClient),
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
