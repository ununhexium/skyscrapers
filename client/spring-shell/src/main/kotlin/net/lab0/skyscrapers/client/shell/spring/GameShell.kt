package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import net.lab0.skyscrapers.client.shell.spring.data.HierarchyResult
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
    @ShellOption(
      "--at",
      help = "Where to place a builder.",
    ) position: Position,
  ): HierarchyResult =
    serverAccessManager.place(position)


  fun buildAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

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
    return serverAccessManager.build(start, target, build)
  }

  fun sealAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

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
    return serverAccessManager.seal(start, target, build)
  }

  fun winAvailability() =
    if (serverAccessManager.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

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
    return serverAccessManager.win(start, target)
  }

  @ShellMethod("Shows the state of the current game.", key = ["state"])
  fun state(): String? {
    return serverAccessManager.state()
  }

}
