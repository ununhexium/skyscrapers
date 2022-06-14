package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent
class GameShell(val serverAccessManager: ServerAccessManager) {

  fun placeAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

  @ShellMethod("Place a builder at the given position.", key = ["place"])
  fun place(
    @ShellOption(help = "Where to place a builder.")
    at: Position,
  ): ShellResult =
    serverAccessManager.place(at)


  fun buildAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

  @ShellMethod("Move a builder and build.", key = ["build"])
  fun build(
    @ShellOption(help = "Which builder to move")
    from: Position,
    @ShellOption(help = "Where to move the builder.")
    to: Position,
    @ShellOption(help = "Where to build.")
    build: Position,
  ): String? {
    return serverAccessManager.build(from, to, build)
  }

  fun sealAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

  @ShellMethod("Move a builder and seal.", key = ["seal"])
  fun seal(
    @ShellOption(help = "Which builder to move")
    from: Position,
    @ShellOption(help = "Where to move the builder.")
    to: Position,
    @ShellOption(help = "Where to seal.")
    seal: Position,
  ): String? {
    return serverAccessManager.seal(from, to, seal)
  }

  fun winAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

  @ShellMethod("Move a builder and win.", key = ["win"])
  fun win(
    @ShellOption(help = "Which builder to move")
    from: Position,
    @ShellOption(help = "Where to move the builder.")
    to: Position,
  ): String? {
    return serverAccessManager.win(from, to)
  }

  @ShellMethod("Shows the state of the current game.", key = ["state"])
  fun state(): String? {
    return serverAccessManager.state()
  }

}
