package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import java.io.Writer

class Play(writer: Writer?) : MyCliktCommand(writer, name = "play") {
  data class GameNameStorage(val game: GameName)

  val game by argument(help = "The name of the game").convert { GameName(it) }
  val token by argument(help = "The token toa ccess the game").convert { AccessToken(it) }

  override fun run() = Unit
}
