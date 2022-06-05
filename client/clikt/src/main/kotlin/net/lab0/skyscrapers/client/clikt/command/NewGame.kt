package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class NewGame(writer: Writer?) :
  MyCliktCommand(writer, name = "new-game"),
  KoinComponent {
  private val name by argument(
    help = "The name of the game"
  ).convert { GameName(it) }

  private val sky by inject<SkyscraperClient>()

  override fun run() {
    sky
      .create(name)
      .bimap(
        { errors -> errors.forEach { myEcho(it) } },
        { myEcho("Created game '$name'.") },
      )
  }
}
