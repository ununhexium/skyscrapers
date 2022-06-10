package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.shell.spring.component.GameMaster
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class MenuShell(val gameMaster: GameMaster) {
// TODO: capture sigint and stop the server if it's running
  @ShellMethod(
    "Choose the server and test the connectivity.",
    key = ["connect"]
  )
  fun connect(
    @ShellOption("--url", help = "The server URL") baseUrl: String,
  ): String? {
    gameMaster.reconnect(baseUrl)

    return gameMaster.status(baseUrl)
  }

  @ShellMethod("Create a new game on the server", key = ["create"])
  fun create(
    @ShellOption(
      "--game",
      help = "The name of the game.",
    ) name: GameName,
  ): String? {
    return gameMaster.create(name)
  }

  fun joinAvailability() =
    if (gameMaster.isConnected()) Availability.available()
    else Availability.unavailable("you must connect to a server first")

  @ShellMethod("Join an existing game.", key = ["join"])
  fun join(
    @ShellOption(
      "--game",
      help = "The name of the game to join.",
    ) name: GameName,
  ): String? {
    return gameMaster.join(name)
  }

}
