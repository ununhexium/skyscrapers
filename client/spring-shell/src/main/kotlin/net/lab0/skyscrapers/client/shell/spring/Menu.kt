package net.lab0.skyscrapers.client.shell.spring

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import javax.validation.constraints.Size


@ShellComponent
class Menu(val factory: SkyscraperClientFactoryComponent) {

  lateinit var client: SkyscraperClient

  @ShellMethod(
    "Choose the server and rest the connectivity.",
    key = ["connect"]
  )
  fun connect(
    @ShellOption("--url", help = "The server URL") baseUrl: String,
  ): String {
    client = factory.newClient(BaseUrl(baseUrl))

    return client.status().map {
      "Connected to $baseUrl.\nAvailable games: " +
          it.games.joinToString(separator = "\n")
    }.mapLeft { status ->
      "Failed to connect to $baseUrl with status $status."
    }.merge()
  }

  @ShellMethod("Create a new game on the server", key = ["create"])
  fun create(
    @ShellOption(
      "--game",
      help = "The name of the game.",
    ) name: GameName,
  ): String {
    return client
      .create(name)
      .map {
        "Created game ${it.name}."
      }
      .mapLeft {
        "Error when creating the game:\n" + it.joinToString(separator = "\n")
      }.merge()
  }

  @ShellMethod("Join an existing game.", key = ["join"])
  fun join(
    @ShellOption(
      "--game",
      help = "The name of the game to join.",
    ) name: GameName,
  ): String {
    return client
      .join(name)
      .map {
        "Joined game ${name.value} as player ${it.player} with access token ${it.token.value}."
      }
      .mapLeft {
        "Error when joining the game:\n" + it.joinToString(separator = "\n")
      }.merge()
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
