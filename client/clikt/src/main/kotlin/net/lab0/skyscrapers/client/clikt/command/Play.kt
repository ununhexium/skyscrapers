package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import java.io.Writer

class Play(writer: Writer?) : MyCliktCommand(writer, name = "play") {
  val game by option("-g", "--game", help = "The name of the game").convert { GameName(it) }.required()

  override fun run() {
    myEcho("$game")
  }
}
