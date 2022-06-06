package net.lab0.skyscrapers.client.shell.spring

import arrow.core.merge
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import javax.validation.constraints.Size


@ShellComponent
class Menu(val factory: SkyscraperClientFactoryComponent) {

  lateinit var client: SkyscraperClient

  @ShellMethod("Choose the server and rest the connectivity.")
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

  @ShellMethod("Add two integers together.")
  fun add(a: Int, b: Int): Int {
    return a + b
  }


  @ShellMethod(value = "Display stuff.", prefix = "-")
  fun echo(a: Int, b: Int, @ShellOption("--third") c: Int): String? {
    return String.format("You said a=%d, b=%d, c=%d", a, b, c)
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
