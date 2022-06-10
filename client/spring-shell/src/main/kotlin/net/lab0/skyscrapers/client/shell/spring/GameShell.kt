package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.component.GameMaster
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent
class GameShell(val gameMaster: GameMaster) {

  fun placeAvailability() =
    if (gameMaster.inGame) Availability.available()
    else Availability.unavailable("you must join a game first")

  @ShellMethod("Place a builder at the given position.", key = ["place"])
  fun place(
    @ShellOption(
      "--at",
      help = "Where to place a builder.",
    ) position: Position,
  ): String? {
    return gameMaster.place(position)
  }


  fun buildAvailability() =
    if (gameMaster.inGame) Availability.available()
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
    return gameMaster.build(start, target, build)
  }

  fun sealAvailability() =
    if (gameMaster.inGame) Availability.available()
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
    return gameMaster.seal(start, target, build)
  }

  fun winAvailability() =
    if (gameMaster.inGame) Availability.available()
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
    return gameMaster.win(start, target)
  }

  @ShellMethod("Shows the state of the current game.", key = ["state"])
  fun state(): String? {
    return gameMaster.state()
  }

}
