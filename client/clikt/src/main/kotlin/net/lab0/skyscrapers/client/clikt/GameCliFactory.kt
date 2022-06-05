package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.client.clikt.command.Configuration
import net.lab0.skyscrapers.client.clikt.command.Connect
import net.lab0.skyscrapers.client.clikt.command.Current
import net.lab0.skyscrapers.client.clikt.command.Join
import net.lab0.skyscrapers.client.clikt.command.NewGame
import net.lab0.skyscrapers.client.clikt.command.Place
import net.lab0.skyscrapers.client.clikt.command.Show
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class GameCliFactory : KoinComponent {
  private val httpHandler by inject<HttpHandler>()

  fun new(
    writer: Writer?,
    configurer: Configurer = Configurer(Constants.configLocation),
    handler: HttpHandler? = null,
  ): CliktCommand {
    val client = {
      SkyscraperClientImpl(httpHandler)
    }

    return GameCli().subcommands(
      // TODO: list available games
      Connect(writer),
      Configuration(writer),
      NewGame(writer, client),
      Show(writer, client),
      Join(writer, client),
      Current(writer),
      Place(writer, client),
    )
  }
}
