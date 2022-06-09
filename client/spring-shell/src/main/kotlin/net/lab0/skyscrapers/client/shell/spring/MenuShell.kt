package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.component.GameMaster
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class MenuShell(val gameMaster: GameMaster) {

  @ShellMethod(
    "Choose the server and rest the connectivity.",
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

  @ShellMethod("Join an existing game.", key = ["join"])
  fun join(
    @ShellOption(
      "--game",
      help = "The name of the game to join.",
    ) name: GameName,
  ): String? {
    return gameMaster.join(name)
  }

  @ShellMethod("Place a builder at the given position.", key = ["place"])
  fun place(
    @ShellOption(
      "--at",
      help = "Where to place a builder.",
    ) position: Position,
  ): String? {
    return gameMaster.place(position)
  }

  @ShellMethod("Move a builder and build.", key = ["build"])
  fun build(
    @ShellOption(
      "--from",
      help = "Which builder to move",
    ) start: Position,
    @ShellOption(
      "--to",
      help = "Where to move the builder.",
    ) target: Position,
    @ShellOption(
      "--build",
      help = "Where to build.",
    ) build: Position,
  ): String? {
    return gameMaster.build(start, target, build)
  }

  @ShellMethod("Move a builder and seal.", key = ["seal"])
  fun seal(
    @ShellOption(
      "--from",
      help = "Which builder to move",
    ) start: Position,
    @ShellOption(
      "--to",
      help = "Where to move the builder.",
    ) target: Position,
    @ShellOption(
      "--seal",
      help = "Where to seal.",
    ) build: Position,
  ): String? {
    return gameMaster.seal(start, target, build)
  }

  @ShellMethod("Move a builder and win.", key = ["win"])
  fun win(
    @ShellOption(
      "--from",
      help = "Which builder to move",
    ) start: Position,
    @ShellOption(
      "--to",
      help = "Where to move the builder.",
    ) target: Position,
  ): String? {
    return gameMaster.win(start, target)
  }

  @ShellMethod("Shows the state of a game", key = ["state"])
  fun state(): String? {
    return gameMaster.state()
  }



  // TODO: commands availabilities
  fun downloadAvailability(): Availability {
    return if (true) Availability.available() else Availability.unavailable(
      "you are not connected"
    )
  }
}
