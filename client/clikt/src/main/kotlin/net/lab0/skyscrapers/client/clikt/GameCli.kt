package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.client.clikt.command.Configuration
import net.lab0.skyscrapers.client.clikt.command.Connect
import net.lab0.skyscrapers.client.clikt.command.Current
import net.lab0.skyscrapers.client.clikt.command.Join
import net.lab0.skyscrapers.client.clikt.command.NewGame
import net.lab0.skyscrapers.client.clikt.command.Place
import net.lab0.skyscrapers.client.clikt.command.PlaceAt
import net.lab0.skyscrapers.client.clikt.command.Play
import net.lab0.skyscrapers.client.clikt.command.Show
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import java.io.Writer

class GameCli : CliktCommand() {

  companion object {

    fun new(
      writer: Writer?,
      configurer: Configurer = Configurer(Constants.configLocation),
      handler: HttpHandler? = null,
    ): CliktCommand {
      val client = {
        SkyscraperClientImpl(
          handler ?: ClientFilters
            .SetBaseUriFrom(
              Uri.of(configurer.loadConfiguration().server.apiUrl)
            ).then(OkHttp())
        )
      }

      return GameCli().subcommands(
        // TODO: list available games
        Connect(writer, client),
        Configuration(writer, configurer),
        NewGame(writer, client),
        Show(writer, client),
        Join(writer, client),
        Current(writer),
        Play(writer).subcommands(
          Place(writer).subcommands(
            PlaceAt(writer, client)
          )
        ),

//        Next(series),
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
