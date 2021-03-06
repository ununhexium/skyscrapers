package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.struct.LastGame
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer
import kotlin.io.path.bufferedWriter

class Join(  writer: Writer?) :
  MyCliktCommand(  writer,  name = "join"),
KoinComponent {
  private val game by argument(help = "The game to join").convert { GameName(it) }

  private val sky by inject<SkyscraperClient>()

  override fun run() {
    val result = sky.join(game)

    result.bimap(
      leftOperation = {
        myEcho(
          "Failed to join the game ${game.value}: $it",
          err = true
        )
      },
      rightOperation = {
        myEcho("Joined the game ${game.value} as ${it.token.value}")

        Constants.lastJoin(game).bufferedWriter().use { bw ->
          bw.write(Json.encodeToString(LastGame(game.value, it.token.value)))
        }
      }
    )
  }
}
