package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class Show(writer: Writer?) :
  MyCliktCommand(writer, name = "show"),
  KoinComponent {
  private val game by argument(help = "The name of the game.")
    .convert { GameName(it) }

  private val sky by inject<SkyscraperClient>()

  override fun run() {
    sky
      .state(game)
      .mapLeft { myEcho(it.joinToString("\n")) }
      .map { myEcho(it.toString()) }
  }
}
