package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Ok
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class MenuShell(val serverAccessManager: ServerAccessManager) {

  // TODO: capture sigint and stop the server if it's running
  @ShellMethod(
    "Choose the server and test the connectivity.",
    key = ["connect"]
  )
  fun connect(
    @ShellOption(help = "The server URL") url: String,
  ): ShellResult.Tree {
    serverAccessManager.reconnect(BaseUrl(url))

    return ShellResult.Tree(
      Ok.Text("Connected"),
      Ok.Text("Connected to $url."),
      serverAccessManager.status()
    )
  }

  @ShellMethod("Create a new game on the server", key = ["create"])
  fun create(
    @ShellOption(help = "The name of the game.") game: GameName,
  ) = serverAccessManager.create(game)

  fun joinAvailability() =
    if (serverAccessManager.isConnected()) Availability.available()
    else Availability.unavailable("you must connect to a server first")

  @ShellMethod("Join an existing game.", key = ["join"])
  fun join(
    @ShellOption(help = "The name of the game to join.") game: GameName,
  ) = serverAccessManager.join(game)

  enum class AiType {
    RANDOM,
    SEQUENTIAL,
  }

  @ShellMethod("Join an existing game as an AI.", key = [Key.Command.aiJoin])
  fun aiJoin(
    @ShellOption(help = "The name of the game to join.") game: GameName,
    @ShellOption(help = "The AI type to use.") type: AiType,
  ) = serverAccessManager.aiJoin(game, type)

}
