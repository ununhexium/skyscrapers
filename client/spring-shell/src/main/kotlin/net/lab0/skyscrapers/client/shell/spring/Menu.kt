package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.component.GameMaster
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import javax.validation.constraints.Size


@ShellComponent
class Menu(val gameMaster: GameMaster) {

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


  @ShellMethod("Add Numbers.", key = ["adds"])
  fun addNumbers(@ShellOption(arity = 3) numbers: FloatArray): Float {
    return numbers[0] + numbers[1] + numbers[2]
  }

  private var connected = false

  @ShellMethod("Change password.")
  fun changePassword(@Size(min = 8, max = 40) password: String): String? {
    return "Password successfully set to $password"
  }

  @ShellMethod("Download the nuclear codes.")
  fun download() {
  }

  fun downloadAvailability(): Availability {
    return if (connected) Availability.available() else Availability.unavailable(
      "you are not connected"
    )
  }
}
